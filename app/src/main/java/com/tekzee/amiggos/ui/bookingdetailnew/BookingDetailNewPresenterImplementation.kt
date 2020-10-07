package com.tekzee.amiggos.ui.bookingdetailnew

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookingDetailNewPresenterImplementation(private var mainView: BookingDetailsNewPresenter.ABookingDetailsPresenterMainView, context: Context): BookingDetailsNewPresenter.ABookingDetailsPresenterMain {


    override fun dogetBookingDetails(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.dogetBookingDetails(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    mainView.hideKeyboard()
                    when (response.code()) {
                        200 -> {
                            val responseData: BookingDetailsNewResponse? = response.body()
                            if (responseData!!.status) {

                                mainView.onBookingDetailSuccess(responseData)
                            } else {
                                mainView.onBookingDetailFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }



    var context: Context? = context
    private var disposable: Disposable? =null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}
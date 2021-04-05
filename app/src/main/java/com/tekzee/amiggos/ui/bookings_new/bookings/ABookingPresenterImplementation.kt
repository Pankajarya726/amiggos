package com.tekzee.amiggos.ui.bookings_new.bookings

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ABookingPresenterImplementation (private var mainView: ABookingPresenter.ABookingPresenterMainView,
                                       context: Context
) : ABookingPresenter.ABookingPresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if (mainView != null)
                mainView.hideProgressbar()
        }
    }

    override fun docallGetBookings(input: JsonObject, createHeaders: HashMap<String, String?>) {
            mainView.showProgress()
            if (mainView.checkInternet()) {
                disposable = ApiClient.instance.docallGetBookings(input,createHeaders)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        mainView.hideProgress()
                        when (response.code()) {
                            200 -> {
                                val responseData: ABookingResponse? = response.body()
                                if (responseData!!.status) {
                                    mainView.onBookingSuccess(responseData.data.bookingData,responseData)
                                } else {
                                    mainView.onBookingFailure(responseData.message)
                                }
                            } 404 -> {
                            mainView.logoutUser()
                        }
                        }
                    }, { error ->
                        mainView.hideProgress()
                        mainView.onBookingFailure(error.message.toString())
                    })
            } else {
                mainView.hideProgress()
                mainView.validateError(context!!.getString(R.string.check_internet))
            }
        }


}
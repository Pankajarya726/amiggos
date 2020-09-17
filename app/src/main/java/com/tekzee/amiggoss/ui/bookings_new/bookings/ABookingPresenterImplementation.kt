package com.tekzee.amiggoss.ui.bookings_new.bookings

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.bookings_new.bookings.model.ABookingResponse
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

            if (mainView.checkInternet()) {
                disposable = ApiClient.instance.docallGetBookings(input,createHeaders)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        mainView.hideProgressbar()
                        when (response.code()) {
                            200 -> {
                                val responseData: ABookingResponse? = response.body()
                                if (responseData!!.status) {
                                    mainView.onBookingSuccess(responseData.data.bookingData)
                                } else {
                                    mainView.onBookingFailure(responseData.message)
                                }
                            }
                        }
                    }, { error ->
                        mainView.hideProgressbar()
                        mainView.onBookingFailure(error.message.toString())
                    })
            } else {
                mainView.hideProgressbar()
                mainView.validateError(context!!.getString(R.string.check_internet))
            }
        }


}
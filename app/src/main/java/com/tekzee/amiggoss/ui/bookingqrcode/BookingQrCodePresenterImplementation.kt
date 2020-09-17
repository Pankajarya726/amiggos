package com.tekzee.amiggoss.ui.bookingqrcode

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.bookingqrcode.model.BookinQrCodeResponse

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class BookingQrCodePresenterImplementation(private var  mainView: BookingQrCodePresenter.BookingQrCodeMainView,context: Context):BookingQrCodePresenter.BookingQrCodeMainPresenter {

    var context: Context? = context
    private var disposable: Disposable? =null


    override fun doGetBookingQrCode(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doGetBookingQrCode(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: BookinQrCodeResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onGetBookingQrCodeSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
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


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}
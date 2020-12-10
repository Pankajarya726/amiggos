package com.tekzee.amiggos.ui.stripepayment.paymentactivity

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.BookingPaymentResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.model.SetDefaultCardResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PaymentActivityImplementation(private var mainView: PaymentActivityPresenter.APaymentMethodPresenterMainView, context: Context): PaymentActivityPresenter.APaymentMethodPresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null




    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    override fun getCardList(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.getCardList(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CardListResponse? = response.body()
                            if (responseData!!.success) {
                                mainView.onCardListSuccess(responseData.data.cards,responseData.customerStripId)
                            } else {
                                mainView.onCardListFailure(responseData)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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

    override fun createBookingPayment(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.createBookingPayment(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: BookingPaymentResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onBookingSuccess(responseData)
                            } else {
                                mainView.onBookingFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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

    override fun setDefaultCard(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.setDefaultCard(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: SetDefaultCardResponse? = response.body()
                            if (responseData!!.success) {
                                mainView.onSetDefaultCardSuccess(responseData.message)
                            } else {
                                mainView.onSetDefaultCardFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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

}
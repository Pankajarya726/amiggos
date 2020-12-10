package com.tekzee.amiggos.stripe

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.stripe.model.APaymentSuccessResponse
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CheckOutActivityImplementation (private var mainView: CheckOutActivityPresenter.CheckOutActivityPresenterMainView, context: Context): CheckOutActivityPresenter.CheckOutActivityPresenterMain {



    var context: Context? = context
    private var disposable: Disposable? =null
//    override fun getPaymentIntentClientSecret(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ) {
//        mainView.showProgressbar()
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.getPaymentIntentClientSecret(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    mainView.hideProgressbar()
//                    val responseCode = response.code()
//                    when (responseCode) {
//                        200 -> {
//                            val responseData: ClientSecretResponse? = response.body()
//                            if (responseData!!.status) {
//                                mainView.onPaymentIntentclientSuccess(responseData)
//                            } else {
//                                mainView.onPaymentIntentclientSuccess(responseData)
//                            }
//                        }
//                    }
//                }, { error ->
//                    mainView.hideProgressbar()
//                    mainView.onPaymentIntentclientFailure(error.message.toString())
//                })
//        } else {
//            mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }

    override fun chargeUser(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.chargeUserApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: APaymentSuccessResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onChargeSuccess(responseData)
                            } else {
                                mainView.onChargeFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onChargeFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun callPaymentByCardId(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.callPaymentByCardId(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: APaymentSuccessResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onChargeSuccess(responseData)
                            } else {
                                mainView.onChargeFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onChargeFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }



    override fun updatePaymentStatus(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.updatePaymentStatus(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUpdatePaymentStatus(responseData)
                            } else {
                                mainView.onUpdatePaymentFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onUpdatePaymentFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
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
                                mainView.validateError(responseData.message)
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


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}
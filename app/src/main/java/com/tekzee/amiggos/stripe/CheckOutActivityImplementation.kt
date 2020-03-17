package com.tekzee.amiggos.stripe

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.stripe.model.ClientSecretResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CheckOutActivityImplementation (private var mainView: CheckOutActivityPresenter.CheckOutActivityPresenterMainView, context: Context): CheckOutActivityPresenter.CheckOutActivityPresenterMain {



    var context: Context? = context
    private var disposable: Disposable? =null
    override fun getPaymentIntentClientSecret(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.getPaymentIntentClientSecret(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: ClientSecretResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onPaymentIntentclientSuccess(responseData)
                            } else {
                                mainView.onPaymentIntentclientSuccess(responseData)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onPaymentIntentclientFailure(error.message.toString())
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

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}
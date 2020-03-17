package com.tekzee.amiggos.ui.stripepayment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class APaymentMehtodImplementation(private var mainView: APaymentMethodPresenter.APaymentMethodPresenterMainView, context: Context): APaymentMethodPresenter.APaymentMethodPresenterMain {

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
                                mainView.onCardListSuccess(responseData.cards.cards)
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

}
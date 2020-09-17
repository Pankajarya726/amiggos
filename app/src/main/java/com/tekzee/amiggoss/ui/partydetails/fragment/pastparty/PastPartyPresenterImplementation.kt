package com.tekzee.amiggoss.ui.partydetails.fragment.pastparty

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggoss.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PastPartyPresenterImplementation (private var mainView: PastPartyPresenter.PastPartyMainView, context: Context):
    PastPartyPresenter.PastPartyMainPresenter {

    private var context: Context? = null
    private var disposable: Disposable? = null






    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }



    override fun docallPastPartyApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallPastPartyApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: PastPartyResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onPastPartSuccess(responseData)
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
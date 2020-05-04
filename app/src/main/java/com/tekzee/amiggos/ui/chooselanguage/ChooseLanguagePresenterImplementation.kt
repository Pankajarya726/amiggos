package com.tekzee.amiggos.ui.chooselanguage

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChooseLanguagePresenterImplementation(private var mainView: ChooseLanguagePresenter.ChooseLanguageMainView,context: Context): ChooseLanguagePresenter.ChooseLanguagePresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null


    override fun doCallLanguageApi(
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallLanguageApi(createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: LanguageResponse? = response.body()
                            if (responseData!!.status) {



                                mainView.onLanguageSuccess(responseData)
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

    override fun doLanguageConstantApi(
        headers: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doLanguageConstantApi(headers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: JsonObject? = response.body()
                            if (responseData!!.get("status").asBoolean) {
                                mainView.onLanguageConstantResponse(responseData.get("data").asJsonObject)
                            } else {
                                mainView.validateError(responseData.get("message").asString)
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
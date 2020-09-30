package com.tekzee.amiggos.ui.mainsplash

import android.content.Context
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainSplashPresenterImplementation(private var mainView: MainSplashPresenter.MainSplashPresenterMainView,context: Context):MainSplashPresenter.MainSplashPresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null




    override fun doValidateAppVersionApi(
        input: JsonObject,
        headers: HashMap<String, String?>
    ) {
//        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doValidateAppVersionApi(input,headers)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        mainView.hideProgressbar()
                        when (response.code()) {
                            200 -> {
                                val responseData: ValidateAppVersionResponse? = response.body()
                                if (responseData!!.status) {
                                    mainView.onValidateAppVersionResponse(responseData)
                                } else {
                                    mainView.validateError(responseData.message)
                                }
                            }
                        }
                    }, { error ->
//                        mainView.hideProgressbar()
                        Logger.d("himanshu"+error);
                        mainView.validateError(error.message.toString())
                    })
        } else {
//            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
 override fun doLanguageConstantApi(
        headers: HashMap<String, String?>
    ) {
//        mainView.showProgressbar()
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
//                        mainView.hideProgressbar()
                        mainView.validateError(error.message.toString())
                    })
        } else {
//            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
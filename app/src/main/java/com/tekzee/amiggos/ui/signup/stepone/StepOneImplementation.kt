package com.tekzee.amiggos.ui.signup.stepone

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class StepOneImplementation(private var mainView: StepOnePresenter.StepOnePresenterMainView, context: Context): StepOnePresenter.StepOnePresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


//    override fun doCallSettingsApi(
//        input: JsonObject,
//        createHeaders: HashMap<String, String?>
//    ) {
//        mainView.showProgressbar()
//        if (mainView.checkInternet()) {
//            disposable = ApiClient.instance.doCallSettingsApi(input,createHeaders)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ response ->
//                    mainView.hideProgressbar()
//                    val responseCode = response.code()
//                    when (responseCode) {
//                        200 -> {
//                            val responseData: SettingsResponse? = response.body()
//                            if (responseData!!.status) {
//                                mainView.onSettingsSuccess(responseData)
//                            } else {
//                                mainView.validateError(responseData.message)
//                            }
//                        }
//                    }
//                }, { error ->
//                    mainView.hideProgressbar()
//                    mainView.validateError(error.message.toString())
//                })
//        } else {
//            mainView.hideProgressbar()
//            mainView.validateError(context!!.getString(R.string.check_internet))
//        }
//    }


    override fun doCallSignupApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallSignupApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UserData? = response.body()
                            if (responseData!!.status) {
                                mainView.onSignupSuccess(responseData.data)
                            } else {
                                mainView.onSignUpFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onSignUpFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


}
package com.tekzee.amiggoss.ui.settings_new

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.viewandeditprofile.model.GetUserProfileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ASettingsPresenterImplementation (private var mainView: ASettingsPresenter.ASettingsPresenterMainView, context: Context): ASettingsPresenter.ASettingsPresenterMain {



    var context: Context? = context
    private var disposable: Disposable? =null

    override fun doCallUserPrfolie(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.callGetProfile(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetUserProfileResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUserProfileSuccess(responseData.data)
                            } else {
                                mainView.onUserProfileFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onUserProfileFailure(error.message.toString())
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
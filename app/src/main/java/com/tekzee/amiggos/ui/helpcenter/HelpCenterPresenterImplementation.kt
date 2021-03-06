package com.tekzee.amiggos.ui.helpcenter

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggos.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HelpCenterPresenterImplementation(private var mainView: HelpCenterPresenter.HelpCenterMainView, context: Context): HelpCenterPresenter.HelpCenterMainPresenter {



    var context: Context? = context
    private var disposable: Disposable? =null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    override fun doCallHelpCenterApi(
    input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallHelpCenterApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: HelpCenterResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onHelpCenterSuccess(responseData)
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




}
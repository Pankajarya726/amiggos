package com.tekzee.amiggoss.ui.agegroup

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.agegroup.model.AgeGroupResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AgeGroupActivityPresenterImplementation(private var mainView: AgeGroupActivityPresenter.AgeGroupMainView,context: Context):AgeGroupActivityPresenter.AgeGroupPresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null

    override fun doCallAgeGroupApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallAgeGroupApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: AgeGroupResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onAgeGroupApiSuccess(responseData)
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

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
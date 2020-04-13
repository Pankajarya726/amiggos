package com.tekzee.amiggos.ui.newpreferences.amusictypefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AMusicTypeImplementation(
    private var mainView: AMusicTypePresenter.AMusicTypePresenterMainView,
    context: Context
) : AMusicTypePresenter.AMusicTypePresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doCallMusicTypeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
     ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallMusicTypeApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: AMusicTypeResponse? = response.body()
                            if (responseData!!.data.musicType.isNotEmpty()) {
                                    mainView.onMusicTypeSuccess(responseData)
                            } else {
                                mainView.onMusicTypeFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onMusicTypeFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
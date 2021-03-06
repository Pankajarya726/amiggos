package com.tekzee.amiggos.ui.viewfriends

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import com.tekzee.amiggos.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ViewFriendPresenterImplementation(private var mainView: ViewFriendPresenter.ViewFriendMainView,context: Context):ViewFriendPresenter.ViewFriendMainPresenter {
    var context: Context? = context
    private var disposable: Disposable? =null

    override fun docallViewFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallViewFriendApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: StorieViewResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onViewFriendSuccess(responseData)
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


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
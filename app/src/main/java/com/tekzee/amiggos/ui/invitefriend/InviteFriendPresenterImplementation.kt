package com.tekzee.amiggos.ui.invitefriend

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.mallortaxi.base.BaseMainView
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.security.AccessControlContext

class InviteFriendPresenterImplementation(
    private var mainView: InviteFriendActivityPresenter.InviteFriendMainView,
    context: Context
) : InviteFriendActivityPresenter.InviteFriendPresenterMain {


    private var context: Context? = null
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doUpdateFriendCount(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doUpdateFriendCount(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: UpdateFriendCountResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUpdateFriendCountSuccess(responseData)
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
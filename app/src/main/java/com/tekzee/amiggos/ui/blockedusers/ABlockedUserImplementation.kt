package com.tekzee.amiggos.ui.blockedusers

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.amiggos.ui.blockedusers.model.UnBlockFriendResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ABlockedUserImplementation(private var mainView: ABlockedUserPresenter.ABlockedUserPresenterMainView, context: Context): ABlockedUserPresenter.ABlockedUserPresenterMain {



    var context: Context? = context
    private var disposable: Disposable? =null
    override fun docallgetblockusers(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallBlockedUser(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: BlockedUserResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onblockusersuccess(responseData.data.blockedUserList)
                            } else {
                                mainView.onblockuserfailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onblockuserfailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun docallunblockusers(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.docallunblockusers(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UnBlockFriendResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onunblocksuccess(responseData.message)
                            } else {
                                mainView.onunblockfailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onunblockfailure(error.message.toString())
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
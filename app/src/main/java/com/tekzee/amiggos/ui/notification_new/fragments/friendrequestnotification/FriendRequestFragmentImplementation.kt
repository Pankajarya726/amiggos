package com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FriendRequestFragmentImplementation(
    private var mainView: FriendRequestFragmentPresenter.FriendRequestFragmentPresenterMainView,
    context: Context
) : FriendRequestFragmentPresenter.FriendRequestFragmentPresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doCallNotificationOne(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallNotification(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: ANotificationResponse? = response.body()
                            if (responseData!!.status) {

                                if (responseData.data.userNotification.isNotEmpty()) {
                                    if(requestDatFromServer){
                                        mainView.onNotificationInfiniteSuccess(responseData.data.userNotification)
                                    }else{
                                        mainView.onNotificationSuccess(responseData.data.userNotification)
                                    }
                                } else {
                                    mainView.onNotificationFailure(responseData.message)
                                }
                            } else {
                                mainView.onNotificationFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onNotificationFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun doAcceptInvitationApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doAcceptInvitationApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onAcceptRequestSuccess(responseData.message)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onNotificationFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }



    override fun callRejectApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doRejectInvitationApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onAcceptRequestSuccess(responseData.message)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onNotificationFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

}
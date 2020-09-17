package com.tekzee.amiggoss.ui.notification_new.fragments.friendrequestnotification

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.notification_new.model.ANotificationResponse
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
        createHeaders: HashMap<String, String?>
    ) {

        mainView.showProgressbar()
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
                                if(responseData.data.userNotification.isNotEmpty()){
                                    mainView.onNotificationSuccess(responseData.data.userNotification)
                                }else{
                                    mainView.onNotificationFailure(responseData.message)
                                }

                            } else {
                                mainView.onNotificationFailure(responseData.message)
                            }
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
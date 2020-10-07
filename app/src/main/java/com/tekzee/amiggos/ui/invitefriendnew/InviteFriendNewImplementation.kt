package com.tekzee.amiggos.ui.invitefriendnew

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.ourmemories.model.InviteFriendResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class InviteFriendNewImplementation(
    private var mainView: InviteFriendNewPresenter.InviteFriendPresenterMainView,
    context: Context
) : InviteFriendNewPresenter.InviteFriendPresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    override fun doCallGetFriends(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if (!requestDatFromServer) {
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallGetFriends(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: InviteFriendResponse? = response.body()
                            if (responseData!!.data.realFreind.isNotEmpty()) {
                                if (requestDatFromServer) {
                                    mainView.onOurMemoriesSuccessInfinite(responseData)
                                } else {
                                    mainView.onOurMemoriesSuccess(responseData)
                                }
                            } else {
                                mainView.onOurMemoriesFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onOurMemoriesFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun doCallInviteFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>

    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doInviteFriend(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onFriendInviteSuccess(responseData.message)
                            } else {
                                mainView.onFriendInviteFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onOurMemoriesFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


}
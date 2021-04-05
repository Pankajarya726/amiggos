package com.tekzee.amiggos.ui.ourmemories

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.ourmemories.model.GetFriendForInviteAfterCreateMemoryResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class InviteFriendImplementation(
    private var mainView: InviteFriendPresenter.InviteFriendPresenterMainView,
    context: Context
) : InviteFriendPresenter.InviteFriendPresenterMain {


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
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
            if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallGetFriendsAfterCreateMemory(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: GetFriendForInviteAfterCreateMemoryResponse? = response.body()
                            if (responseData!!.data.realFreind.isNotEmpty()) {
                                if(requestDatFromServer){
                                    mainView.onOurMemoriesSuccessInfinite(responseData)
                                }else{
                                    mainView.onOurMemoriesSuccess(responseData)
                                }
                            } else {
                                mainView.onOurMemoriesFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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
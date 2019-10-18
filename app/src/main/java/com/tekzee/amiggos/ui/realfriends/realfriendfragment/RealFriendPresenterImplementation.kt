package com.tekzee.amiggos.ui.realfriends.realfriendfragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RealFriendPresenterImplementation(
    private var mainView: RealFriendPresenter.RealFriendMainView,
    context: Context
) : RealFriendPresenter.RealFriendMainPresenter {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doCallRealFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallRealFriendApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: RealFriendResponse? = response.body()
                            if (responseData!!.data.size>0) {
                                if(requestDatFromServer){
                                    mainView.onRealFriendInfiniteSuccess(responseData)
                                }else{
                                    mainView.onRealFriendSuccess(responseData)
                                }
                            } else {
                                mainView.onRealFriendFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onRealFriendFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
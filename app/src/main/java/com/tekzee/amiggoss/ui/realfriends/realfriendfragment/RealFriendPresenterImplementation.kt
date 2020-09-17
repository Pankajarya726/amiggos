package com.tekzee.amiggoss.ui.realfriends.realfriendfragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggoss.network.ApiClient
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
            mainView.hideProgressbar()
        }
    }

    override fun doCallRealFriendApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean,
        fragmentVisible: Boolean
    ) {
        if(!requestDatFromServer && fragmentVisible){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallRealFriendApiV2(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: RealFriendV2Response? = response.body()
                            if (responseData!!.data.realFreind.isNotEmpty()) {
                                if(requestDatFromServer){
                                    mainView.onRealFriendInfiniteSuccess(responseData.data.realFreind)
                                }else{
                                    mainView.onRealFriendSuccess(responseData.data.realFreind)
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
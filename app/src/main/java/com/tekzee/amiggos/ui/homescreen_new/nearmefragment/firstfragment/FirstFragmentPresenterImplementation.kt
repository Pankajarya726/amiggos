package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FirstFragmentPresenterImplementation (private var mainView: FirstFragmentPresenter.FirstFragmentPresenterMainView,
context: Context
) : FirstFragmentPresenter.FirstFragmentPresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun getNearByUser(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.getNearByUser(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: SearchFriendResponse? = response.body()
                            if (responseData!!.data.isNotEmpty()) {
                                if(requestDatFromServer){
                                    mainView.onOnlineFriendInfiniteSuccess(responseData)
                                }else{
                                    mainView.onOnlineFriendSuccess(responseData)
                                }
                            } else {
                                mainView.onOnlineFriendFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onOnlineFriendFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
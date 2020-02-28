package com.tekzee.amiggos.ui.memories.ourmemories

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OurMemoriePresenterImplementation(
    private var mainView: OurMemoriePresenter.OurMemoriePresenterMainView,
    context: Context
) : OurMemoriePresenter.OurMemoriePresenterrMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if (mainView != null)
                mainView.hideProgressbar()
        }
    }


    override fun doGetMyStories(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
//        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doGetMyStories(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: GetMyStoriesResponse? = response.body()
                            if (responseData!!.status) {
                                if(requestDatFromServer){
                                    mainView.onMyStoriesInfiniteSuccess(responseData)
                                }else{
                                    mainView.onMyStoriesSuccess(responseData)
                                }
                            } else {
                                mainView.onMyStoriesFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    //                    mainView.hideProgressbar()
                    mainView.onMyStoriesFailure(error.message.toString())
                })
        } else {
//            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun getNearByUser(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if (!requestDatFromServer) {
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.getNearByUserv2(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<NearByV2Response> ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: NearByV2Response? = response.body()
                            if (responseData!!.data.nearestFreind.isNotEmpty()) {
                                if (requestDatFromServer) {
                                    mainView.onOnlineFriendInfiniteSuccess(responseData.data.nearestFreind)
                                } else {
                                    mainView.onOnlineFriendSuccess(responseData.data.nearestFreind)
                                }
                            } else {
                                mainView.onOnlineFriendFailure(responseData.message)
                            }
                        }
                        404 -> {
                            Utility.showLogoutPopup(
                                context!!,
                                "your Session has been expired,please logout"
                            )
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
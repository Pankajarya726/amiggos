package com.tekzee.amiggos.ui.home

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.amiggos.ui.home.model.VenueResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeActivityPresenterImplementation(private var mainView: HomeActivityPresenter.HomeActivityMainView,context: Context):HomeActivityPresenter.HomeActivityPresenterMain {



    private var context: Context? = null
    private var disposable: Disposable? = null

    override fun doGetVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        isfirsttime: Boolean
    ) {

//        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doGetVenueApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: VenueResponse? = response.body()
                            if (responseData!!.status) {
                                if(isfirsttime){
                                    mainView.onVenueResponseInfiniteSuccess(responseData)
                                }else{
                                    mainView.onVenueResponse(responseData)
                                }
                            } else {
                                mainView.onVenueFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
//                    mainView.hideProgressbar()
                    mainView.onVenueFailure(error.message.toString())
                })
        } else {
//            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }

    }

    override fun doGetDashboardMapApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doGetDashboardMapApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: DashboardReponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onDashboardMapResponse(responseData)
                            } else {
                                mainView.onDashboardMapFailure(responseData.message)
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

    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }
}
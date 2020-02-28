package com.tekzee.amiggos.ui.memories_new.venuefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.home.model.VenueResponse
import com.tekzee.mallortaxi.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class VenueFragmentPresenterImplementation (private var mainView: VenueFragmentPresenter.VenueFragmentPresenterMainView,
                                            context: Context
) : VenueFragmentPresenter.VenueFragmentPresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doGetVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        isfirsttime: Boolean
    ) {
        if(!isfirsttime)
        mainView.showProgressbar()

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
                    if(!isfirsttime)
                    mainView.hideProgressbar()
                    mainView.onVenueFailure(error.message.toString())
                })
        } else {
            if(!isfirsttime)
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }

    }
}
package com.tekzee.amiggos.ui.venuedetailsnew

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AVenueDetailsPresenterImplementation(private var mainView: AVenueDetailsPresenter.AVenueDetailsPresenterMainView, context: Context): AVenueDetailsPresenter.AVenueDetailsPresenterMain {

    var context: Context? = context
    private var disposable: Disposable? =null




    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    override fun callVenueDetailsApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.callGetVenueDetails(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: VenueDetails? = response.body()
                            if (responseData!!.status) {
                                mainView.onVenueDetailsSuccess(responseData.data)
                            } else {
                                mainView.onVenueDetailsFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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


    override fun callLikeUnlikeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.callLikeUnlikeApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onLikeUnlikeSuccess(responseData.message)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
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
}
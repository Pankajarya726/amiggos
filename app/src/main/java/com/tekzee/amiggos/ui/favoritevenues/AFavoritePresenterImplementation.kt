package com.tekzee.amiggos.ui.favoritevenues

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AFavoritePresenterImplementation(
    private var mainView: AFavoriteVenuePresenter.AFavoriteVenuePresenterMainView,
    context: Context
) : AFavoriteVenuePresenter.AFavoriteVenuePresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
            if(mainView!=null)
                mainView.hideProgressbar()
        }
    }

    override fun doCallFavoriteVenueApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>,
        requestDatFromServer: Boolean
    ) {
        if(!requestDatFromServer){
            mainView.showProgressbar()
        }
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallFriendsFavoriteVenueV2(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: FavoriteVenueResponse? = response.body()
                            if (responseData!!.data.favoriteVenue.isNotEmpty()) {
                                if(requestDatFromServer){
                                    mainView.onFavoriteVenueInfiniteSuccess(responseData!!.data.favoriteVenue)
                                }else{
                                    mainView.onFavoriteVenueSuccess(responseData!!.data.favoriteVenue)
                                }
                            } else {
                                mainView.onFavoriteVenueFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onFavoriteVenueFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
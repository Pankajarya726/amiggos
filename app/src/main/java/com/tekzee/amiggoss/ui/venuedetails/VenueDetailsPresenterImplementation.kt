package com.tekzee.amiggoss.ui.venuedetails

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class VenueDetailsPresenterImplementation(private var mainView: VenueDetailsPresenter.VenueDetailsMainView,context: Context): VenueDetailsPresenter.VenueDetailsMainPresenter {

    var context: Context? = context
    private var disposable: Disposable? =null




    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }


    override fun doCallWeekApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallWeekApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: ChooseWeekResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onChooseWeekSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
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

}
package com.tekzee.amiggos.ui.partydetails.fragment.upcommingparty

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpcomingPartyPresenterImplementation(private var mainView: UpcommingPartyPresenter.UpcomingPartyMainView,context: Context): UpcommingPartyPresenter.UpcomingPartyMainPresenter {


    private var context: Context? = null
    private var disposable: Disposable? = null






    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }



    override fun doCallUpcomingPartyApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {

        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallUpcomingPartyApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: PastPartyResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUpcomingPartySuccess(responseData)
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
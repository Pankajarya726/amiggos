package com.tekzee.amiggos.ui.newpreferences.avenuetypefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AVenueTypeImplementation(
    private var mainView: AVenueTypePresenter.AVenueTypePresenterMainView,
    context: Context
) : AVenueTypePresenter.AVenueTypePresenterMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

    override fun doCallVenueTypeApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
     ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallVenueTypeApi(input,createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    when (response.code()) {
                        200 -> {
                            val responseData: AVenueTypeResponse? = response.body()
                            if (responseData!!.data.venueType.isNotEmpty()) {
                                    mainView.onVenueTypeSuccess(responseData)
                            } else {
                                mainView.onVenueTypeFailure(responseData.message)
                            }
                        } 404 -> {
                        mainView.logoutUser()
                    }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onVenueTypeFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }
}
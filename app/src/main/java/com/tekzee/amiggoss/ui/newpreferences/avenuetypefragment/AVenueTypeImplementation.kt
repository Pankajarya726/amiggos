package com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.network.ApiClient
import com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
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
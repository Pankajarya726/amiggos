package com.tekzee.amiggos.ui.memories.venuefragment

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.memories.venuefragment.model.VenueTaggedResponse
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
            if (mainView != null)
                mainView.hideProgressbar()
        }
    }

    override fun callTaggedVenueApi(input: JsonObject, createHeaders: HashMap<String, String?>) {

            if (mainView.checkInternet()) {
                disposable = ApiClient.instance.callTaggedVenueApi(input,createHeaders)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        // mainView.hideProgressbar()
                        when (response.code()) {
                            200 -> {
                                val responseData: MemorieResponse? = response.body()
                                if (responseData!!.status) {
                                    mainView.onVenueResponse(responseData.data.memoriesList)
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
                //mainView.hideProgressbar()
                mainView.validateError(context!!.getString(R.string.check_internet))
            }
        }


}
package com.tekzee.amiggos.ui.venuedetails

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.amiggos.network.ApiClient
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
}
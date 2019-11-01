package com.tekzee.amiggos.ui.venuedetails

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.mallortaxi.base.BaseMainView

class VenueDetailsPresenter {

    interface VenueDetailsMainPresenter{
        fun onStop()
    }

    interface VenueDetailsMainView: BaseMainView{

    }
}
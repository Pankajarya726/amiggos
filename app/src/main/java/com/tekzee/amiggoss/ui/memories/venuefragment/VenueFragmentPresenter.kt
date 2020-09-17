package com.tekzee.amiggoss.ui.memories.venuefragment

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.memories.venuefragment.model.VenueTaggedResponse
import com.tekzee.mallortaxi.base.BaseMainView

class VenueFragmentPresenter{

    interface VenueFragmentPresenterMain{
        fun onStop()
        fun callTaggedVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface VenueFragmentPresenterMainView: BaseMainView {
        fun onVenueResponse(taggedVenue: List<VenueTaggedResponse.Data.TaggedVenue>)
        fun onVenueFailure(message: String)
    }
}
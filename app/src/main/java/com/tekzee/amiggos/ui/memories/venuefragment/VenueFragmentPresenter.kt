package com.tekzee.amiggos.ui.memories.venuefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

class VenueFragmentPresenter{

    interface VenueFragmentPresenterMain{
        fun onStop()
        fun callTaggedVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface VenueFragmentPresenterMainView: BaseMainView {
        fun onVenueResponse(taggedVenue: List<MemorieResponse.Data.Memories>)
        fun onVenueFailure(message: String)
    }
}
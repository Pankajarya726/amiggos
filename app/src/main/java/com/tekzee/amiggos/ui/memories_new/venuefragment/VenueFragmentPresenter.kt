package com.tekzee.amiggos.ui.memories_new.venuefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class VenueFragmentPresenter{

    interface VenueFragmentPresenterMain{
        fun onStop()
        fun doGetVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            isfirsttime: Boolean
        )

    }

    interface VenueFragmentPresenterMainView: BaseMainView {
        fun onVenueResponse(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?)
        fun onVenueResponseInfiniteSuccess(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?)
        fun onVenueFailure(message: String)
    }
}
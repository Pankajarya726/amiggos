package com.tekzee.amiggos.ui.venuedetailsnew

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails

class AVenueDetailsPresenter {

    interface AVenueDetailsPresenterMain{
        fun onStop()
        fun callVenueDetailsApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun callLikeUnlikeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AVenueDetailsPresenterMainView: BaseMainView {
        fun onVenueDetailsSuccess(responseData: VenueDetails.Data)
        fun onVenueDetailsFailure(message: String)
        fun onLikeUnlikeSuccess(message: String)
    }
}
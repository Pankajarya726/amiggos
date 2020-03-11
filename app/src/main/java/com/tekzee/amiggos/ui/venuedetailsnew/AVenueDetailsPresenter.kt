package com.tekzee.amiggos.ui.venuedetailsnew

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.mallortaxi.base.BaseMainView

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

    interface AVenueDetailsPresenterMainView: BaseMainView{
        fun onVenueDetailsSuccess(responseData: ClubDetailResponse.Data)
        fun onLikeUnlikeSuccess(message: String)
    }
}
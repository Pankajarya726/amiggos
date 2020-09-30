package com.tekzee.amiggos.ui.favoritevenues

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.amiggos.base.BaseMainView

class AFavoriteVenuePresenter {

    interface AFavoriteVenuePresenterMain{
        fun onStop()
        fun doCallFavoriteVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface AFavoriteVenuePresenterMainView : BaseMainView {
        fun onFavoriteVenueSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>)
        fun onFavoriteVenueInfiniteSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>)
        fun onFavoriteVenueFailure(message: String)
    }

}
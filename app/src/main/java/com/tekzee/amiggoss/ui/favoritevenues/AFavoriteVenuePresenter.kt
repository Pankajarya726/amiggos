package com.tekzee.amiggoss.ui.favoritevenues

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.mallortaxi.base.BaseMainView

class AFavoriteVenuePresenter {

    interface AFavoriteVenuePresenterMain{
        fun onStop()
        fun doCallFavoriteVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface AFavoriteVenuePresenterMainView : BaseMainView{
        fun onFavoriteVenueSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>)
        fun onFavoriteVenueInfiniteSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>)
        fun onFavoriteVenueFailure(message: String)
    }

}
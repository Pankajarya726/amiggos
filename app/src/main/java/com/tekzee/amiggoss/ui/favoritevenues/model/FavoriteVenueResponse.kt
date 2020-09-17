package com.tekzee.amiggoss.ui.favoritevenues.model


import com.google.gson.annotations.SerializedName

data class FavoriteVenueResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("favorite_venue")
        var favoriteVenue: List<FavoriteVenue> = listOf()
    ) {
        data class FavoriteVenue(
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("image")
            var image: String = "",
            val loadingStatus: Boolean = false
        )
    }
}
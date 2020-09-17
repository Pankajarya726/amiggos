package com.tekzee.amiggoss.ui.venuedetailsnew.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClubData(
    @SerializedName("club_basic_details")
    var clubBasicDetails: String = "",
    @SerializedName("club_name")
    var clubName: String = "",
    @SerializedName("club_state_city")
    var clubStateCity: String = "",
    @SerializedName("club_id")
    var clubId: String = "",
    @SerializedName("club_image")
    var clubImage: String = "",
    @SerializedName("club_type")
    var clubType: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("dress")
    var dress: String = "",
    @SerializedName("club_description")
    var club_description: String = "",
    @SerializedName("isFavoriteVenue")
    var isFavoriteVenue: Boolean = false,
    @SerializedName("agelimit")
    var agelimit: String = ""
):Serializable
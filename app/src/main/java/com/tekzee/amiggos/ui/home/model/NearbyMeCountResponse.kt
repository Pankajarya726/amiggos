package com.tekzee.amiggos.ui.home.model


import com.google.gson.annotations.SerializedName

data class NearbyMeCountResponse(
    @SerializedName("data")
    val `data`: NearByMeCountData = NearByMeCountData(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
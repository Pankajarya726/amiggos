package com.tekzee.amiggoss.ui.home.model


import com.google.gson.annotations.SerializedName

data class NearByMeCountData(
    @SerializedName("nearByUserCount")
    val nearByUserCount: Int = 0
)
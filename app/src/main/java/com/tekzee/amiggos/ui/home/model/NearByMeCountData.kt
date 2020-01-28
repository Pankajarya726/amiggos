package com.tekzee.amiggos.ui.home.model


import com.google.gson.annotations.SerializedName

data class NearByMeCountData(
    @SerializedName("nearByUserCount")
    val nearByUserCount: Int = 0
)
package com.tekzee.amiggoss.ui.home.model


import com.google.gson.annotations.SerializedName

data class UpdateFriendCountResponse(
    @SerializedName("data")
    val `data`: List<DataX> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
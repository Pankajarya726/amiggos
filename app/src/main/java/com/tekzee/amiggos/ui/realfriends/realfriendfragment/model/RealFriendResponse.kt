package com.tekzee.amiggos.ui.realfriends.realfriendfragment.model


import com.google.gson.annotations.SerializedName

data class RealFriendResponse(
    @SerializedName("data")
    val `data`: List<RealFriendData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
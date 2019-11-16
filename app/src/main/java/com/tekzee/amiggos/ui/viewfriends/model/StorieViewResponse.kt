package com.tekzee.amiggos.ui.viewfriends.model


import com.google.gson.annotations.SerializedName

data class StorieViewResponse(
    @SerializedName("data")
    val `data`: List<StorieViewData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
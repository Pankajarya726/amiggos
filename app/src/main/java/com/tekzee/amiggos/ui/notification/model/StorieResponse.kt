package com.tekzee.amiggos.ui.notification.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.home.model.StoriesData

data class StorieResponse(
    @SerializedName("data")
    val `data`: List<StoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
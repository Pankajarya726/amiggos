package com.tekzee.amiggoss.ui.notification.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggoss.ui.home.model.StoriesData

data class StorieResponse(
    @SerializedName("data")
    val `data`: List<StoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
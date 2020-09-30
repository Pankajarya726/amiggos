package com.tekzee.amiggos.ui.turningup.model


import com.google.gson.annotations.SerializedName

data class TurningUpResponse(
    @SerializedName("data")
    val `data`: List<TurningUpData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
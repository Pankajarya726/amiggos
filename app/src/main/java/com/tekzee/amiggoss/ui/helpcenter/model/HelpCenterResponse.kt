package com.tekzee.amiggoss.ui.helpcenter.model


import com.google.gson.annotations.SerializedName

data class HelpCenterResponse(
    @SerializedName("data")
    val `data`: List<HelpCenterData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
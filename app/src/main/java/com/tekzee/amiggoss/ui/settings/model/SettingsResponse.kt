package com.tekzee.amiggoss.ui.settings.model


import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    val `data`: List<SettingsData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
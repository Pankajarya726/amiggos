package com.tekzee.amiggoss.ui.settings.model


import com.google.gson.annotations.SerializedName

data class UpdateSettingsResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
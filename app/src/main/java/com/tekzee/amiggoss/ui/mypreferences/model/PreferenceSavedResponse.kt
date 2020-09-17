package com.tekzee.amiggoss.ui.mypreferences.model


import com.google.gson.annotations.SerializedName

data class PreferenceSavedResponse(
    @SerializedName("data")
    val `data`: List<Any> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
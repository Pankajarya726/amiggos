package com.tekzee.amiggos.ui.mypreferences.model


import com.google.gson.annotations.SerializedName

data class MyPreferenceResponse(
    @SerializedName("data")
    val `data`: List<MyPreferenceData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
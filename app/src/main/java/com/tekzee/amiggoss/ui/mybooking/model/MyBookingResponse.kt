package com.tekzee.amiggoss.ui.mybooking.model


import com.google.gson.annotations.SerializedName

data class MyBookingResponse(
    @SerializedName("data")
    val `data`: List<MyBookingData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
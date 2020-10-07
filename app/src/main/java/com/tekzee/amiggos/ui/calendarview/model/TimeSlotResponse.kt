package com.tekzee.amiggos.ui.calendarview.model


import com.google.gson.annotations.SerializedName

data class TimeSlotResponse(
    @SerializedName("data")
    val `data`: List<String> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
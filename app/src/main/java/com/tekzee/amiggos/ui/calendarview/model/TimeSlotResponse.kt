package com.tekzee.amiggos.ui.calendarview.model


import com.google.gson.annotations.SerializedName

data class TimeSlotResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("timeSlot")
        val timeSlot: List<String> = listOf()
    )
}
package com.tekzee.amiggoss.ui.chooseweek.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChooseWeekResponse(
    @SerializedName("data")
    val `data`: CalendarData = CalendarData(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable
package com.tekzee.amiggoss.ui.chooseweek.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CalendarData(
    @SerializedName("calender")
    val calender: List<Calender> = listOf(),
    @SerializedName("day_data")
    val dayData: List<DayData> = listOf()
): Serializable
package com.tekzee.amiggoss.ui.chooseweek.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DayData(
    @SerializedName("date")
    val date: String = "",
    @SerializedName("value")
    val value: String = ""
): Serializable
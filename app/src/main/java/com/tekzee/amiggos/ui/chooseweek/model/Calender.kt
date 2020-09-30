package com.tekzee.amiggos.ui.chooseweek.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Calender(
    @SerializedName("closing_time")
    val closingTime: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("dd")
    val dd: String = "",
    @SerializedName("is_open")
    val isOpen: Int = 0,
    @SerializedName("opening_time")
    val openingTime: String = "",
    @SerializedName("value")
    val value: String = ""
): Serializable
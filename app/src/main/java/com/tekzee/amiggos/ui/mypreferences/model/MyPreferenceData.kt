package com.tekzee.amiggos.ui.mypreferences.model

import com.google.gson.annotations.SerializedName

data class MyPreferenceData(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("response_key")
    val responseKey: String = "",
    @SerializedName("select_max")
    val selectMax: String = "",
    @SerializedName("subtitle")
    val subtitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("values")
    val values: List<Value> = listOf()
)
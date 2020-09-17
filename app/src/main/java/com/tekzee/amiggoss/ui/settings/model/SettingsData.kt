package com.tekzee.amiggoss.ui.settings.model


import com.google.gson.annotations.SerializedName

data class SettingsData(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("is_set")
    val isSet: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = ""
)
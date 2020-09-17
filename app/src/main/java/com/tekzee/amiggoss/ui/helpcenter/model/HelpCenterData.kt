package com.tekzee.amiggoss.ui.helpcenter.model


import com.google.gson.annotations.SerializedName

data class HelpCenterData(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = ""
)
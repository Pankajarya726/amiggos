package com.tekzee.amiggos.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class PackageResponse(
    @SerializedName("data")
    val `data`: List<PackageData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
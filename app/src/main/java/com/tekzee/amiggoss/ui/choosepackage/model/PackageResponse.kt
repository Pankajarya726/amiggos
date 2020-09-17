package com.tekzee.amiggoss.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class PackageResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
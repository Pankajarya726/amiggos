package com.tekzee.amiggoss.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class PackageBookResponse(
    @SerializedName("data")
    val `data`: PackageBookData = PackageBookData(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
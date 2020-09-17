package com.tekzee.amiggoss.base.model


import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
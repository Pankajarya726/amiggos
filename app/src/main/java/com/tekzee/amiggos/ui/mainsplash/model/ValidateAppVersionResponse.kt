package com.tekzee.amiggos.ui.mainsplash.model


import com.google.gson.annotations.SerializedName

data class ValidateAppVersionResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false,
    @SerializedName("update_type")
    val updateType: Int = 0
)
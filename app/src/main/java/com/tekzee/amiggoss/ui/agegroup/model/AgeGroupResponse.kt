package com.tekzee.amiggoss.ui.agegroup.model


import com.google.gson.annotations.SerializedName

data class AgeGroupResponse(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
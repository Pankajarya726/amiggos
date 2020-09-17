package com.tekzee.amiggoss.ui.stripepayment.model


import com.google.gson.annotations.SerializedName

data class DeleteCardResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
)
package com.tekzee.amiggoss.ui.bookingqrcode.model


import com.google.gson.annotations.SerializedName

data class BookinQrCodeResponse(
    @SerializedName("data")
    val `data`: BookingQrCodeData = BookingQrCodeData(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
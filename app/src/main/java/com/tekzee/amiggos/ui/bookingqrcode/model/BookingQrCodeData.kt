package com.tekzee.amiggos.ui.bookingqrcode.model


import com.google.gson.annotations.SerializedName

data class BookingQrCodeData(
    @SerializedName("booking_code")
    val bookingCode: String = "",
    @SerializedName("bottom_text")
    val bottomText: String = "",
    @SerializedName("qr_code")
    val qrCode: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = ""
)
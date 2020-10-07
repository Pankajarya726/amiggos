package com.tekzee.amiggos.ui.stripepayment.paymentactivity.model


import com.google.gson.annotations.SerializedName

data class BookingPaymentResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("booking_id")
        val bookingId: String = ""
    )
}
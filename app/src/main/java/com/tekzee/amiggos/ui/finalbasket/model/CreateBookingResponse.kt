package com.tekzee.amiggos.ui.finalbasket.model


import com.google.gson.annotations.SerializedName

data class CreateBookingResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("booking")
        val booking: Booking = Booking()
    ) {
        data class Booking(
            @SerializedName("amount")
            val amount: Float = 0.0f,
            @SerializedName("booking_id")
            val bookingId: Float = 0.0f
        )
    }
}
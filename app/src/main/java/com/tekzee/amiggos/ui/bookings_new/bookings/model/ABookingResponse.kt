package com.tekzee.amiggos.ui.bookings_new.bookings.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ABookingResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("bookings")
        var bookingData: List<BookingData> = listOf()
    ):Serializable {
        data class BookingData(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("venue_id")
            var venue_id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("booking_date")
            var booking_date: String = "",
            @SerializedName("booking_time")
            var booking_time: String = "",
            @SerializedName("venue_home_image")
            var venue_home_image: String ="",
            @SerializedName("booking_status")
            var booking_status: String = ""
        ):Serializable
    }
}
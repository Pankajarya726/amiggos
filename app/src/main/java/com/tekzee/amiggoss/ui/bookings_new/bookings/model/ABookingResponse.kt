package com.tekzee.amiggoss.ui.bookings_new.bookings.model


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
        @SerializedName("booking_data")
        var bookingData: List<BookingData> = listOf()
    ):Serializable {
        data class BookingData(
            @SerializedName("booking_code")
            var bookingCode: String = "",
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("date_time")
            var dateTime: String = "",
            @SerializedName("end_time")
            var endTime: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("package_id")
            var packageId: Int = 0,
            @SerializedName("party_date")
            var partyDate: String = "",
            @SerializedName("price")
            var price: Int = 0,
            @SerializedName("qr_code")
            var qrCode: String = "",
            @SerializedName("start_time")
            var startTime: String = "",
            @SerializedName("symbol_left")
            var symbolLeft: String = "",
            @SerializedName("symbol_right")
            var symbolRight: String = "",
            @SerializedName("user_id")
            var userId: Int = 0
        ):Serializable
    }
}
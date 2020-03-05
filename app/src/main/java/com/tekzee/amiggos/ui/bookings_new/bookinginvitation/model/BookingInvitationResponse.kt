package com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model


import com.google.gson.annotations.SerializedName

data class BookingInvitationResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("booking_details")
        var bookingDetails: List<BookingDetail> = listOf()
    ) {
        data class BookingDetail(
            @SerializedName("booking_id")
            var bookingId: Int = 0,
            @SerializedName("club_address")
            var clubAddress: String = "",
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("party_date")
            var partyDate: String = "",
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("start_time")
            var startTime: String = "",
            @SerializedName("typeoflogin")
            var typeoflogin: String = ""
        )
    }
}
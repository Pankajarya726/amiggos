package com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model


import com.google.gson.annotations.SerializedName

data class BookingInvitationResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("booking_details")
        val bookingDetails: List<BookingDetail> = listOf()
    ) {
        data class BookingDetail(
            @SerializedName("address")
            val address: String = "",
            @SerializedName("booking_date")
            val bookingDate: String = "",
            @SerializedName("booking_id")
            val bookingId: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("party_date")
            val partyDate: String = "",
            @SerializedName("profile")
            val profile: String = "",
            @SerializedName("venue_address")
            val venueAddress: String = "",
            @SerializedName("venue_id")
            val venueId: Int = 0,
            @SerializedName("user_id")
            val user_id: Int = 0,
            @SerializedName("venue_name")
            val venueName: String = ""
        )
    }
}
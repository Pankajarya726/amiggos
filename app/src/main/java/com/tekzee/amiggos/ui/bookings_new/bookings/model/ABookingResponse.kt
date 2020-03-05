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
) {
    data class Data(
        @SerializedName("upcoming_parties")
        var upcomingParties: List<UpcomingParty> = listOf()
    ) {
        data class UpcomingParty(
            @SerializedName("booking_id")
            var bookingId: Int = 0,
            @SerializedName("club_address")
            var clubAddress: String = "",
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("end_time")
            var endTime: String = "",
            @SerializedName("is_party_owner")
            var isPartyOwner: Int = 0,
            @SerializedName("party_date")
            var partyDate: String = "",
            @SerializedName("start_time")
            var startTime: String = "",
            @SerializedName("user_id")
            var userId: Int = 0,
            @SerializedName("venue_home_image")
            var venueHomeImage: String = ""
        ):Serializable
    }
}
package com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model


import com.google.gson.annotations.SerializedName

data class PastPartyData(
    @SerializedName("booking_id")
    val bookingId: Int = 0,
    @SerializedName("club_address")
    val clubAddress: String = "",
    @SerializedName("club_id")
    val clubId: Int = 0,
    @SerializedName("is_party_owner")
    val is_party_owner: Int = 0,
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("party_date")
    val partyDate: String = "",
    @SerializedName("start_time")
    val startTime: String = "",
    @SerializedName("venue_home_image")
    val venueHomeImage: Any = Any()
)
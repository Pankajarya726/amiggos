package com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.model


import com.google.gson.annotations.SerializedName

data class PartyInvitesData(
    @SerializedName("booking_id")
    val bookingId: Int = 0,
    @SerializedName("club_address")
    val clubAddress: String = "",
    @SerializedName("club_id")
    val clubId: Int = 0,
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("party_date")
    val partyDate: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("start_time")
    val startTime: String = ""
)
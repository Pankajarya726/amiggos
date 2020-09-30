package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model


import com.google.gson.annotations.SerializedName

data class PartyInvitesResponse(
    @SerializedName("data")
    val `data`: List<PartyInvitesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
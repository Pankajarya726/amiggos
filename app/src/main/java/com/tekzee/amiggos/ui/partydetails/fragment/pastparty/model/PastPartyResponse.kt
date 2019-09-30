package com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model


import com.google.gson.annotations.SerializedName

data class PastPartyResponse(
    @SerializedName("data")
    val `data`: List<PastPartyData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
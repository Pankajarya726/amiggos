package com.tekzee.amiggos.ui.realfriends.invitations.model


import com.google.gson.annotations.SerializedName

data class InvitationResponse(
    @SerializedName("data")
    val `data`: List<InvitationData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
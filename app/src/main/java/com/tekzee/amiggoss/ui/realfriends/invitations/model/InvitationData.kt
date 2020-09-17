package com.tekzee.amiggoss.ui.realfriends.invitations.model


import com.google.gson.annotations.SerializedName

data class InvitationData(
    @SerializedName("email")
    val email: String = "",
    @SerializedName("firebase_id")
    val firebaseId: String = "",
    @SerializedName("is_friend")
    val isFriend: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("userid")
    val userid: Int = 0
)
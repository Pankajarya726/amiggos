package com.tekzee.amiggos.ui.friendprofile.model


import com.google.gson.annotations.SerializedName

data class FriendProfileResponse(
    @SerializedName("data")
    val `data`: List<FriendProfileData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
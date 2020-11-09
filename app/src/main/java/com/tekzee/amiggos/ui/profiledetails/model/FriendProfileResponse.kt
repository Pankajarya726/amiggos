package com.tekzee.amiggos.ui.profiledetails.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileData

data class FriendProfileResponse(
    @SerializedName("data")
    val `data`: List<FriendProfileData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
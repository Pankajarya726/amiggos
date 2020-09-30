package com.tekzee.amiggos.ui.friendlist.model


import com.google.gson.annotations.SerializedName

data class FriendListResponse(
    @SerializedName("data")
    val `data`: List<FriendListData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
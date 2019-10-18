package com.tekzee.amiggos.ui.friendprofile.model


import com.google.gson.annotations.SerializedName

data class UserFriendData(
    @SerializedName("friend_id")
    val friendId: Int = 0,
    @SerializedName("notification_key")
    val notificationKey: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
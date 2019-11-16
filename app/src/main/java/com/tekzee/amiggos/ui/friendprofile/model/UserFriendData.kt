package com.tekzee.amiggos.ui.friendprofile.model


import com.google.gson.annotations.SerializedName

data class UserFriendData(
    @SerializedName("friend_id")
    val friendId: Int = 0,
    @SerializedName("notification_key")
    val notificationKey: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0,
    @SerializedName("our_story_id")
    val our_story_id: Int = 0,
    @SerializedName("creator_id")
    val creator_id: Int = 0
)
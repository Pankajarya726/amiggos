package com.tekzee.amiggoss.ui.friendlist.model


import com.google.gson.annotations.SerializedName

data class FriendListData(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("friend_status")
    val friendStatus: String = "",
    @SerializedName("is_friend")
    val isFriend: Int = 0,
    @SerializedName("is_invited")
    val isInvited: Int = 0,
    @SerializedName("is_relate")
    val isRelate: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
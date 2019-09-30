package com.tekzee.amiggos.ui.home.model


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("invite_friend_count")
    val inviteFriendCount: Int = 0,
    @SerializedName("is_freind_invities")
    val isFreindInvities: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("remaining_count")
    val remainingCount: Int = 0,
    @SerializedName("required_count")
    val requiredCount: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
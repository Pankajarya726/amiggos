package com.tekzee.amiggoss.ui.mainsplash.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("age")
    val age: String = "",
    @SerializedName("get_package_count")
    val getPackageCount: String = "",
    @SerializedName("invite_friend_count")
    val inviteFriendCount: String = "",
    @SerializedName("is_freind_invities")
    val isFreindInvities: String = "",
    @SerializedName("no_day_register")
    val noDayRegister: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("Status_message")
    val statusMessage: String = "",
    @SerializedName("update_type")
    val updateType: String = ""
)
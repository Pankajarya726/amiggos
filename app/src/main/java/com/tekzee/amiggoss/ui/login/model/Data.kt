package com.tekzee.amiggoss.ui.login.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    var accessToken: String = "",
    @SerializedName("api_token")
    var apiToken: String = "",
    @SerializedName("dob")
    var dob: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("gender")
    var gender: String = "",
    @SerializedName("invite_friend_count")
    var inviteFriendCount: String = "",
    @SerializedName("invite_message")
    var inviteMessage: String = "",
    @SerializedName("is_freind_invities")
    var isFreindInvities: Int = 0,
    @SerializedName("is_profile")
    var isProfile: Int = 0,
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("profile")
    var profile: String = "",
    @SerializedName("referencecode")
    var referencecode: String = "",
    @SerializedName("s_id")
    var sId: String = "",
    @SerializedName("social_login")
    var socialLogin: String = "",
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("tot_invities")
    var totInvities: String = "",
    @SerializedName("typeoflogin")
    var typeoflogin: String = "",
    @SerializedName("userid")
    var userid: Int = 0
)
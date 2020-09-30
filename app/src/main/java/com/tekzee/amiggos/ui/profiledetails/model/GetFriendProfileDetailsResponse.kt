package com.tekzee.amiggos.ui.profiledetails.model


import com.google.gson.annotations.SerializedName

data class GetFriendProfileDetailsResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("address")
        var address: String = "",
        @SerializedName("isMyFriend")
        var isMyFriend: String = "",
        @SerializedName("isMyFriendBlocked")
        var isMyFriendBlocked: String = "",
        @SerializedName("last_name")
        var lastName: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("profile")
        val profile: List<String> = listOf(),
        @SerializedName("social_login")
        var socialLogin: String = "",
        @SerializedName("typeoflogin")
        var typeoflogin: String = "",
        @SerializedName("real_freind_count")
        var real_freind_count: String = "",
        @SerializedName("userid")
        var userid: Int = 0,
        @SerializedName("1")
        var x1: Int = 0
    )
}
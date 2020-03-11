package com.tekzee.amiggos.ui.blockedusers.model


import com.google.gson.annotations.SerializedName

data class BlockedUserResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("blocked_user_list")
        var blockedUserList: List<BlockedUser> = listOf()
    ) {
        data class BlockedUser(
            @SerializedName("name")
            var name: String = "",
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("userid")
            var userid: Int = 0,
            @SerializedName("address")
            var address: String = "",
            @SerializedName("isMyFriend")
            var isMyFriend: Boolean = false,
            @SerializedName("isMyFriendBlocked")
            var isMyFriendBlocked: Boolean = false,
            @SerializedName("real_freind_count")
            var real_freind_count: String = "0"
        )
    }
}
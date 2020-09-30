package com.tekzee.amiggos.ui.realfriends.invitations.model


import com.google.gson.annotations.SerializedName

data class InvitationResponseV2(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("freind_request")
        var freindRequest: List<FreindRequest> = listOf()
    ) {
        data class FreindRequest(
            @SerializedName("name")
            var name: String = "",
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("userid")
            var userid: Int = 0,
            @SerializedName("real_freind_count")
            var real_freind_count: String = "",
            @SerializedName("isMyFriend")
            var isMyFriend: Boolean = false,
            @SerializedName("isMyFriendBlocked")
            var isMyFriendBlocked: Boolean = false,
            @SerializedName("address")
            var address: String = ""
        )
    }
}
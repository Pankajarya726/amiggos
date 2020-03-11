package com.tekzee.amiggos.ui.realfriends.realfriendfragment.model


import com.google.gson.annotations.SerializedName

data class RealFriendV2Response(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("real_freind")
        var realFreind: List<RealFreind> = listOf()
    ) {
        data class RealFreind(
            @SerializedName("name")
            var name: String = "",
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("real_freind_count")
            var real_freind_count: String = "",
            @SerializedName("address")
            var address: String = "",
            @SerializedName("userid")
            var userid: Int = 0,
            @SerializedName("isMyFriend")
            var isMyFriend: Boolean = false,
            @SerializedName("isMyFriendBlocked")
            var isMyFriendBlocked: Boolean = false,

            val loadingStatus: Boolean = false
        )
    }
}
package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model


import com.google.gson.annotations.SerializedName

data class NearByV2Response(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("nearest_freind")
        var nearestFreind: List<NearestFreind> = listOf()
    ) {
        data class NearestFreind(
            @SerializedName("distance_from_mylocation")
            var distanceFromMylocation: Double = 0.0,
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("name")
            var name: String = "",
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
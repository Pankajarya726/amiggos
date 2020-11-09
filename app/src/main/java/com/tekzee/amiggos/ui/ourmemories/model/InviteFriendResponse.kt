//package com.tekzee.amiggos.ui.ourmemories.model
//
//
//import com.google.gson.annotations.SerializedName
//
//data class InviteFriendResponse(
//    @SerializedName("data")
//    val `data`: Data = Data(),
//    @SerializedName("message")
//    val message: String = "",
//    @SerializedName("status")
//    val status: Boolean = false
//) {
//    data class Data(
//        @SerializedName("real_freind")
//        val realFreind: List<RealFreind> = listOf()
//    ) {
//        data class RealFreind(
//            @SerializedName("address")
//            val address: String = "",
//            @SerializedName("isMyFriend")
//            val isMyFriend: String = "",
//            @SerializedName("isMyFriendBlocked")
//            val isMyFriendBlocked: String = "",
//            @SerializedName("name")
//            val name: String = "",
//            @SerializedName("profile")
//            val profile: String = "",
//            @SerializedName("real_freind_count")
//            val realFreindCount: Int = 0,
//            @SerializedName("userid")
//            val userid: Int = 0,
//            val loadingStatus: Boolean = false
//        )
//    }
//}
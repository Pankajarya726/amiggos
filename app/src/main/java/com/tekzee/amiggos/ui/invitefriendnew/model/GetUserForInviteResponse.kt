package com.tekzee.amiggos.ui.invitefriendnew.model


import com.google.gson.annotations.SerializedName

data class GetUserForInviteResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("user")
        val user: List<User> = listOf()
    ) {
        data class User(
            @SerializedName("age")
            val age: Int = 0,
            @SerializedName("booking_id")
            val bookingId: String = "",
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
            @SerializedName("longitude")
            val longitude: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("profile")
            val profile: String = "",
            @SerializedName("status")
            val status: Int = 0,
            @SerializedName("userid")
            val userid: Int = 0,
            val loadingStatus: Boolean = false
        )
    }
}
package com.tekzee.amiggos.ui.profiledetails.model


import com.google.gson.annotations.SerializedName

data class GetFriendProfileDetailsResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("address")
        val address: String = "",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("isMyFriend")
        val isMyFriend: String = "",
        @SerializedName("isMyFriendBlocked")
        val isMyFriendBlocked: String = "",
        @SerializedName("last_name")
        val lastName: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("other_images")
        val otherImages: String = "",
        @SerializedName("profile")
        val profile: List<String> = listOf(),
        @SerializedName("real_freind_count")
        val realFreindCount: Int = 0,
        @SerializedName("state")
        val state: String = "",
        @SerializedName("userid")
        val userid: Int = 0,
        @SerializedName("unique_user_id")
        val unique_user_id: String = "0"
    )
}
package com.tekzee.amiggos.ui.viewandeditprofile.model


import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(
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
        @SerializedName("age")
        val age: Int = 0,
        @SerializedName("api_token")
        val apiToken: String = "",
        @SerializedName("city")
        val city: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("device_type")
        val deviceType: String = "",
        @SerializedName("dob")
        val dob: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("firebase_id")
        val firebaseId: Any? = null,
        @SerializedName("is_profile_complete")
        val isProfileComplete: Int = 0,
        @SerializedName("last_name")
        val lastName: String = "",
        @SerializedName("message_receive")
        val messageReceive: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("profile")
        val profile: String = "",
        @SerializedName("profile_name")
        val profileName: String = "",
        @SerializedName("pronouns")
        val pronouns: String = "",
        @SerializedName("push_notification")
        val pushNotification: Int = 0,
        @SerializedName("state")
        val state: String = "",
        @SerializedName("state_id")
        val stateId: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("userid")
        val userid: Int = 0,
        @SerializedName("username")
        val username: String = "",
        @SerializedName("other_images")
        val otherImages: List<OtherImage> = ArrayList(),
        @SerializedName("visible_map")
        val visibleMap: Int = 0
    ){
        data class OtherImage(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("image")
            val image: String = ""
        )
    }
}
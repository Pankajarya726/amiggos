package com.tekzee.amiggos.ui.signup.steptwo.model


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("api_token")
        val apiToken: String = "",
        @SerializedName("device_id")
        val deviceId: String = "",
        @SerializedName("profile")
        val profile: String = "",
        @SerializedName("device_type")
        val deviceType: String = "",
        @SerializedName("dob")
        val dob: String = "",
        @SerializedName("email")
        val email: String = "",
        @SerializedName("firebase_id")
        val firebaseId: String = "",
        @SerializedName("is_active")
        val isActive: Int = 0,
        @SerializedName("is_profile_complete")
        val isProfileComplete: Int = 0,
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("type")
        val type: String = "",
        @SerializedName("userid")
        val userid: Int = 0,
        @SerializedName("age")
        val age: String = "",
        @SerializedName("myid")
        var myid: Boolean = false,
        @SerializedName("username")
        val username: String = ""
    )
}
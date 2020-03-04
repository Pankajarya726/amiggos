package com.tekzee.amiggos.ui.signup.steptwo.model


import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("api_token")
        var apiToken: String = "",
        @SerializedName("city")
        var city: String = "",
        @SerializedName("device_id")
        var deviceId: String = "",
        @SerializedName("device_type")
        var deviceType: Int = 0,
        @SerializedName("dob")
        var dob: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("is_active")
        var isActive: Int = 0,
        @SerializedName("last_name")
        var lastName: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("state")
        var state: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("userid")
        var userid: String = "",
        @SerializedName("username")
        var username: String = ""
    )
}
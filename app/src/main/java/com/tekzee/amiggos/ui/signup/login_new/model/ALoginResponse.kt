package com.tekzee.amiggos.ui.signup.login_new.model


import com.google.gson.annotations.SerializedName

data class ALoginResponse(
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
        @SerializedName("dob")
        var dob: String = "",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("last_name")
        var lastName: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("profile")
        var profile: String = "",
        @SerializedName("state")
        var state: String = "",
        @SerializedName("typeoflogin")
        var typeoflogin: String = "",
        @SerializedName("userid")
        var userid: Int = 0,
        @SerializedName("username")
        var username: String = "",
        @SerializedName("is_profile_complete")
        var is_profile_complete: Int = 0,
        @SerializedName("age")
        var age: Int = 0,
        @SerializedName("myid")
        var myid: Boolean = false,
        @SerializedName("type")
        var type: String = ""
    )
}
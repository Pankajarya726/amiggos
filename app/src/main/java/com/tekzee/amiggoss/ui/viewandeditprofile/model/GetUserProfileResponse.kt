package com.tekzee.amiggoss.ui.viewandeditprofile.model


import com.google.gson.annotations.SerializedName

data class GetUserProfileResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("state_id")
        var state_id: String = "",
        @SerializedName("city_id")
        var city_id: String = "",
        @SerializedName("city")
        var city: String = "",
        @SerializedName("dob")
        var dob: String = "",
        @SerializedName("last_name")
        var lastName: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("age")
        var age: String = "",
        @SerializedName("address")
        var address: String = "",
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("profile")
        var profile: String = "",
        @SerializedName("social_login")
        var socialLogin: String = "",
        @SerializedName("state")
        var state: String = "",
        @SerializedName("typeoflogin")
        var typeoflogin: String = "",
        @SerializedName("userid")
        var userid: Int = 0
    )
}
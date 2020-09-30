package com.tekzee.amiggos.ui.myprofile.model


import com.google.gson.annotations.SerializedName

data class MyProfileData(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("firebase_id")
    val firebaseId: String = "",
    @SerializedName("id_proof")
    val idProof: String = "",
    @SerializedName("is_venue")
    val isVenue: Int = 0,
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("referencecode")
    val referencecode: String = "",
    @SerializedName("social_login")
    val socialLogin: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
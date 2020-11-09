package com.tekzee.amiggos.ui.friendprofile.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.profiledetails.model.IsRelateOurMemory

data class FriendProfileData(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("firebase_id")
    val firebaseId: String = "",
    @SerializedName("is_relate")
    val isRelate: Int = 0,
    @SerializedName("is_relate_our_memory")
    val isRelateOurMemory: IsRelateOurMemory = IsRelateOurMemory(),
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
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
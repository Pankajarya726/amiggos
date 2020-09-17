package com.tekzee.amiggoss.ui.searchamiggos.model


import com.google.gson.annotations.SerializedName

data class SearchFriendData(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("distance_from_mylocation")
    val distanceFromMylocation: Double = 0.0,
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
    @SerializedName("our_story_invite")
    val ourStoryInvite: Int = 0,
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("userid")
    val userid: Int = 0,
    val loadingStatus: Boolean = false
)
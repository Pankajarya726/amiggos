package com.tekzee.amiggos.ui.turningup.model


import com.google.gson.annotations.SerializedName

data class TurningUpData(
    @SerializedName("age")
    val age: Int = 0,
    @SerializedName("booking_id")
    val bookingId: Int = 0,
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
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
    @SerializedName("meta_key")
    val metaKey: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("party_date")
    val partyDate: String = "",
    @SerializedName("party_start")
    val partyStart: String = "",
    @SerializedName("party_time")
    val partyTime: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0
)
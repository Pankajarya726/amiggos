package com.tekzee.amiggos.ui.homescreen_new.homefragment.model


import com.google.gson.annotations.SerializedName

data class HomeResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("venue")
        val venue: ArrayList<Venue> = ArrayList()
    ) {
        data class Venue(
            @SerializedName("agelimit")
            val agelimit: String = "",
            @SerializedName("distance_from_mylocation")
            val distanceFromMylocation: Double = 0.0,
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("image")
            val image: String = "",
            @SerializedName("latitude")
            val latitude: String = "",
            @SerializedName("longitude")
            val longitude: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("near_by_count")
            val nearByCount: String = "",
            @SerializedName("city")
            val city: String = "",
            @SerializedName("state")
            val state: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("is_google_venue")
            val is_google_venue: Int = 0
        )
    }
}
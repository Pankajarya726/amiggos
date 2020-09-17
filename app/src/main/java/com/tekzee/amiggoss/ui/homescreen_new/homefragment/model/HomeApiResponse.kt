package com.tekzee.amiggoss.ui.homescreen_new.homefragment.model


import com.google.gson.annotations.SerializedName

data class HomeApiResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("notification_count")
        var notificationcount: String = "0",
        @SerializedName("nearest_clubs")
        var nearestClubs: List<NearestClub> = ArrayList(),
        @SerializedName("nearest_user")
    var nearestUser: List<NearestUser> = listOf()
    ) {
        data class NearestClub(
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("type")
            var type: Int = 1,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("club_type")
            var clubType: String = "",
            @SerializedName("distance_from_mylocation")
            var distanceFromMylocation: Double = 0.0,
            @SerializedName("image")
            var image: String = "",
            @SerializedName("is_amigo_club")
            var isAmigoClub: String = "",
            @SerializedName("latitude")
            var latitude: String = "",
            @SerializedName("longitude")
            var longitude: String = "",
            @SerializedName("agelimit")
            var agelimit: String = "",
            @SerializedName("address")
            var address: String = "",
            @SerializedName("dress")
            var dress: String = "",
            @SerializedName("club_description")
            var club_description: String = "",
            @SerializedName("isFavoriteVenue")
            var isFavoriteVenue: Boolean = false,
            @SerializedName("venue_type")
            var venueType: String = ""
        )
        data class NearestUser(
            @SerializedName("address")
            var address: String = "",
            @SerializedName("distance_from_mylocation")
            var distanceFromMylocation: Double = 0.0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("profile")
            var profile: String = "",
            @SerializedName("userid")
            var userid: Int = 0,
            @SerializedName("latitude")
            var latitude: String = "",
            @SerializedName("longitude")
            var longitude: String = ""
        )
    }
}
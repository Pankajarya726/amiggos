package com.tekzee.amiggos.ui.mylifestyle.model


import com.google.gson.annotations.SerializedName

data class AMyLifestyleResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("nearest_clubs")
        var nearestClubs: List<NearestClub> = listOf()
    ) {
        data class NearestClub(
            @SerializedName("address")
            var address: String = "",
            @SerializedName("agelimit")
            var agelimit: Int = 0,
            @SerializedName("club_description")
            var clubDescription: String = "",
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("club_type")
            var clubType: String = "",
            @SerializedName("club_working_value")
            var clubWorkingValue: String = "",
            @SerializedName("distance_from_mylocation")
            var distanceFromMylocation: Double = 0.0,
            @SerializedName("dress")
            var dress: String = "",
            @SerializedName("dress_value")
            var dressValue: String = "",
            @SerializedName("image")
            var image: String = "",
            @SerializedName("is_amigo_club")
            var isAmigoClub: String = "",
            @SerializedName("isFavoriteVenue")
            var isFavoriteVenue: String = "",
            @SerializedName("latitude")
            var latitude: String = "",
            @SerializedName("longitude")
            var longitude: String = "",
            @SerializedName("music_value")
            var musicValue: String = "",
            @SerializedName("time_value")
            var timeValue: String = "",
            @SerializedName("venue_type")
            var venueType: String = ""
        )
    }
}
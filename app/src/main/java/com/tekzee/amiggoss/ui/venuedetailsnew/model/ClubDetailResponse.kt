package com.tekzee.amiggoss.ui.venuedetailsnew.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ClubDetailResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("club_data")
        var clubData: List<ClubData> = listOf()
    ):Serializable {
        data class ClubData(
            @SerializedName("age_group")
            var ageGroup: String = "",
            @SerializedName("city")
            var city: String = "",
            @SerializedName("club_description")
            var clubDescription: String = "",
            @SerializedName("club_name")
            var clubName: String = "",
            @SerializedName("home_image")
            var homeImage: String = "",
            @SerializedName("image_video_data")
            var imageVideoData: List<ImageVideoData> = listOf(),
            @SerializedName("is_open")
            var isOpen: Int = 0,
            @SerializedName("pos")
            var pos: Int = 0,
            @SerializedName("state")
            var state: String = "",
            @SerializedName("value")
            var value: String = "",
            @SerializedName("venue_data")
            var venueData: List<VenueData> = listOf()
        ):Serializable {
            data class ImageVideoData(
                @SerializedName("is_image")
                var isImage: Int = 0,
                @SerializedName("url")
                var url: String = ""
            ):Serializable

            data class VenueData(
                @SerializedName("image_icon")
                var imageIcon: String = "",
                @SerializedName("value")
                var value: String = ""
            ):Serializable
        }
    }
}
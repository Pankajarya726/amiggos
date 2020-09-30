package com.tekzee.amiggos.ui.imagepanaroma.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VenueDetailData(
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("image_video_data")
    val imageVideoData: List<ImageVideoData> = listOf(),
    @SerializedName("is_open")
    val isOpen: Int = 0,
    @SerializedName("pos")
    val pos: Int = 0,
    @SerializedName("value")
    val value: String = "",
    @SerializedName("venue_data")
    val venueData: List<VenueData> = listOf()
):Serializable
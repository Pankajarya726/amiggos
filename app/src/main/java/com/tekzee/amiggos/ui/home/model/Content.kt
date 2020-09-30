package com.tekzee.amiggos.ui.home.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Content(
    @SerializedName("api_url")
    val apiUrl: String = "",
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("hours")
    val hours: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("minutes")
    val minutes: Int = 0,
    @SerializedName("thumb_video")
    val thumbVideo: String = "",
    @SerializedName("time_ago")
    val timeAgo: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("video_thumb")
    val videoThumb: String = "",
    @SerializedName("viewCount")
    val viewCount: Int = 0
):Serializable
package com.tekzee.amiggos.ui.memories.ourmemories.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Content(
    @SerializedName("api_url")
    var apiUrl: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("hours")
    var hours: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("minutes")
    var minutes: Int = 0,
    @SerializedName("thumb_video")
    var thumbVideo: String = "",
    @SerializedName("time_ago")
    var timeAgo: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("video_thumb")
    var videoThumb: String = "",
    @SerializedName("viewCount")
    var viewCount: Int = 0
):Serializable
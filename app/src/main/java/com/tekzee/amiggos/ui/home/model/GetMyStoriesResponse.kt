package com.tekzee.amiggos.ui.home.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetMyStoriesResponse(
    @SerializedName("data")
    val `data`: List<StoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable
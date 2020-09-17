package com.tekzee.amiggoss.ui.home.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoriesData(
    var loadingStatus: Boolean = false,
    @SerializedName("api_url")
    val apiUrl: String = "",
    @SerializedName("content")
    val content: ArrayList<Content> = ArrayList<Content>(),
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0,
    @SerializedName("our_story_id")
    val our_story_id: Int = 0
):Serializable
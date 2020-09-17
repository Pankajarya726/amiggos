package com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggoss.ui.home.model.Content

data class OurMemoriesData(
    @SerializedName("api_url")
    val apiUrl: String = "",
    @SerializedName("content")
    val content: List<Content> = listOf(),
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("userid")
    val userid: Int = 0,
    val loadingStatus: Boolean = false
)
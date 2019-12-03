package com.tekzee.amiggos.ui.mymemories.fragment.memories.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.home.model.StoriesData

data class MyMemoriesResponse(
    @SerializedName("data")
    val `data`: List<StoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
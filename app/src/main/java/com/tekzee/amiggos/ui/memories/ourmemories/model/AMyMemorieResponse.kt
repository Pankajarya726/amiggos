package com.tekzee.amiggos.ui.memories.ourmemories.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.home.model.StoriesData
import java.io.Serializable

data class AMyMemorieResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
):Serializable

data class Data(
    @SerializedName("my_story_list")
    var myStoryList: List<StoriesData> = listOf()
):Serializable
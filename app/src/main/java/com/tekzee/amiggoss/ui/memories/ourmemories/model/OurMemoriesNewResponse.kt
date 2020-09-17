package com.tekzee.amiggoss.ui.memories.ourmemories.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OurMemoriesNewResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("my_story_list")
        var myStoryList: List<MyStory> = listOf()
    ):Serializable
}
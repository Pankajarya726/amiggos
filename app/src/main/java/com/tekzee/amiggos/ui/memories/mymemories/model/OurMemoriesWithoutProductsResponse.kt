package com.tekzee.amiggos.ui.memories.mymemories.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.home.model.StoriesData

data class OurMemoriesWithoutProductsResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("our_story")
        var ourStory: List<StoriesData> = listOf()
    )
}
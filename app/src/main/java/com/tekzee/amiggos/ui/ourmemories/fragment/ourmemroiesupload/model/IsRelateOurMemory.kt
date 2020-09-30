package com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model


import com.google.gson.annotations.SerializedName

data class IsRelateOurMemory(
    @SerializedName("is_our_story")
    val isOurStory: Int = 0,
    @SerializedName("our_story_id")
    val ourStoryId: String = ""
)
package com.tekzee.amiggos.ui.memories.ourmemories.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyStory(
    @SerializedName("content")
    var content: ArrayList<Content>,
    @SerializedName("imageUrl")
    var imageUrl: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("userid")
    var userid: Int = 0
): Serializable
package com.tekzee.amiggos.ui.imagepanaroma.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageVideoData(
    @SerializedName("is_image")
    val isImage: Int = 0,
    @SerializedName("url")
    val url: String = ""
):Serializable
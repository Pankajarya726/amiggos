package com.tekzee.amiggos.ui.attachid.model


import com.google.gson.annotations.SerializedName

data class MyIdResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("image_available")
        val imageAvailable: Boolean = false,
        @SerializedName("photo_id")
        val photoId: String = ""
    )
}
package com.tekzee.amiggos.ui.viewandeditprofile.model


import com.google.gson.annotations.SerializedName

data class AddImageResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("other_images")
        val otherImages: List<GetUserProfileResponse.Data.OtherImage> = listOf()
    )
}
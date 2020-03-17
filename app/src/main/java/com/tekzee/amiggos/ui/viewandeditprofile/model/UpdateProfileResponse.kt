package com.tekzee.amiggos.ui.viewandeditprofile.model


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    class Data(
        @SerializedName("user_image")
        var user_image: String = ""
    )
}
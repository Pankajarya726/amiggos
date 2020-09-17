package com.tekzee.amiggoss.ui.blockedusers.model


import com.google.gson.annotations.SerializedName

data class UnBlockFriendResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    class Data(
    )
}
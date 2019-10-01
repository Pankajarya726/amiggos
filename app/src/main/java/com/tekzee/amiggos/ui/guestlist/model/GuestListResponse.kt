package com.tekzee.amiggos.ui.guestlist.model


import com.google.gson.annotations.SerializedName

data class GuestListResponse(
    @SerializedName("data")
    val `data`: List<GuestListData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
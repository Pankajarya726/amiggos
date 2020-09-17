package com.tekzee.amiggoss.ui.ourmemories.fragment.ourmemroiesupload.model


import com.google.gson.annotations.SerializedName

data class OurFriendListResponse(
    @SerializedName("data")
    val `data`: List<OurFriendListData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
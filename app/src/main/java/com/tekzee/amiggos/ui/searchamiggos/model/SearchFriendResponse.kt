package com.tekzee.amiggos.ui.searchamiggos.model


import com.google.gson.annotations.SerializedName

data class SearchFriendResponse(
    @SerializedName("data")
    val `data`: List<SearchFriendData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
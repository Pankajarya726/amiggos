package com.tekzee.amiggos.ui.mymemories.fragment.memories.model


import com.google.gson.annotations.SerializedName

data class MyMemoriesResponse(
    @SerializedName("data")
    val `data`: List<MemoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
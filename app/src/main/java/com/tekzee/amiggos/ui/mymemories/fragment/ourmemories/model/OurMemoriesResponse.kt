package com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model


import com.google.gson.annotations.SerializedName

data class OurMemoriesResponse(
    @SerializedName("data")
    val `data`: List<OurMemoriesData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
)
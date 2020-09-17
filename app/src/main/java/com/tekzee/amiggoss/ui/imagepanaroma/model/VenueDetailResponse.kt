package com.tekzee.amiggoss.ui.imagepanaroma.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VenueDetailResponse(
    @SerializedName("data")
    val `data`: List<VenueDetailData> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable
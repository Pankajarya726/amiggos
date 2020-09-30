package com.tekzee.amiggos.ui.imagepanaroma.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VenueData(
    @SerializedName("image_icon")
    val imageIcon: String = "",
    @SerializedName("value")
    val value: String = ""
):Serializable
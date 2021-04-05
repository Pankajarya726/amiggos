package com.tekzee.amiggos.ui.memories.venuefragment.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import java.io.Serializable

data class VenueTaggedResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
): Serializable {
    data class Data(
        @SerializedName("venue_detail")
        var taggedVenue: List<MemorieResponse.Data.Memories> = listOf()
    ): Serializable
}
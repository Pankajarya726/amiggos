package com.tekzee.amiggoss.ui.memories.venuefragment.model


import com.google.gson.annotations.SerializedName

data class VenueTaggedResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("tagged_venue")
        var taggedVenue: List<TaggedVenue> = listOf()
    ) {
        data class TaggedVenue(
            @SerializedName("club_id")
            var clubId: Int = 0,
            @SerializedName("image")
            var image: String = "",
            @SerializedName("name")
            var name: String = ""
        )
    }
}
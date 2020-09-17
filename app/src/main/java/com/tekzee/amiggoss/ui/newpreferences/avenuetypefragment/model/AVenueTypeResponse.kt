package com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.model


import com.google.gson.annotations.SerializedName

data class AVenueTypeResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("venue_type")
        var venueType: List<VenueType> = listOf()
    ) {
        data class VenueType(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("venue_type")
            var venueType: String = ""
        )
    }
}
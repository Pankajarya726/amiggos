package com.tekzee.amiggos.ui.home.model

data class VenueResponse(
    val data: DataVenueResponse = DataVenueResponse(),
    val message: String = "",
    val status: Boolean = false
)
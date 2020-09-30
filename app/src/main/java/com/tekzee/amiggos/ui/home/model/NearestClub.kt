package com.tekzee.amiggos.ui.home.model

data class NearestClub(
    var loadingStatus: Boolean = false,
    val address: String = "",
    val agelimit: String = "",
    val aspect_ratio: String = "",
    val city: String = "",
    val club_id: Int = 0,
    val club_name: String = "",
    val club_type: String = "",
    val country: String = "",
    val distance_from_mylocation: Double = 0.0,
    val image: String = "",
    val is_regular_image: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val music: Any = Any(),
    val music_type: String = "",
    val music_value: String = "",
    val panaroma_Image: String = "",
    val state: String = "",
    val venue_type: String = "",
    val vip_table: Int = 0,
    val zipcode: String = ""


)
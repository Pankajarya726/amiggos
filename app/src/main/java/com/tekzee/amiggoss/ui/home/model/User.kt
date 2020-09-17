package com.tekzee.amiggoss.ui.home.model

data class User(
    val age: Int = 0,
    val bottle_icon: Int = 0,
    val distance_from_mylocation: Double = 0.0,
    val dob: String = "",
    val firebase_id: String = "",
    val freinds_agegroup: Int = 0,
    val is_relate: Int = 0,
    val is_relate_our_memory: IsRelateOurMemory = IsRelateOurMemory(),
    val latitude: String = "",
    val location: String = "",
    val longitude: String = "",
    val name: String = "",
    val profile: String = "",
    val userid: Int = 0
)
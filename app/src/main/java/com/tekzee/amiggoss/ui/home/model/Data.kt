package com.tekzee.amiggoss.ui.home.model

data class Data(
    val notification_count: Int = 0,
    val users: List<User> = ArrayList<User>()
)
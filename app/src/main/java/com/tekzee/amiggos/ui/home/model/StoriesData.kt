package com.tekzee.amiggos.ui.home.model

data class StoriesData(
    var loadingStatus: Boolean = false,
    val api_url: String = "",
    val content: List<Any> = ArrayList<Any>(),
    val imageUrl: String = "",
    val name: String = "",
    val status: Int = 0,
    val userid: Int = 0
)
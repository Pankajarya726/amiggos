package com.tekzee.amiggos.ui.home.model

data class GetMyStoriesResponse(
    val `data`: ArrayList<StoriesData>,
    val message: String = "",
    val status: Boolean = false
)
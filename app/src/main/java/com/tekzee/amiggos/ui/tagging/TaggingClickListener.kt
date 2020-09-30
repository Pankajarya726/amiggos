package com.tekzee.amiggosvenueapp.ui.tagging

import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse

interface TaggingClickListener {
    fun onItemClicked(
        position: Int,
        listItem: TaggingResponse.Data.Search
    )
}
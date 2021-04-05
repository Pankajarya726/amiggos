package com.tekzee.amiggosvenueapp.ui.tagging

import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse

interface TaggingRecyclerviewClickListener {
    fun onItemCloseClicked(
        position: Int,
        listItem: TaggingResponse.Data.Search
    )
}
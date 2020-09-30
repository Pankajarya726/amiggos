package com.tekzee.amiggosvenueapp.ui.tagging.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse

class TaggingDiffutil : DiffUtil.ItemCallback<TaggingResponse.Data.Search>() {
    override fun areItemsTheSame(oldItem: TaggingResponse.Data.Search, newItem: TaggingResponse.Data.Search): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TaggingResponse.Data.Search, newItem: TaggingResponse.Data.Search): Boolean {
        return oldItem == newItem
    }


}
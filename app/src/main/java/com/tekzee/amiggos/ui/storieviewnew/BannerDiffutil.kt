package com.tekzee.amiggosvenueapp.ui.storieview

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse


class BannerDiffutil : DiffUtil.ItemCallback<MemorieResponse.Data.Memories.Memory.Tagged>() {
    override fun areItemsTheSame(oldItem: MemorieResponse.Data.Memories.Memory.Tagged, newItem: MemorieResponse.Data.Memories.Memory.Tagged): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MemorieResponse.Data.Memories.Memory.Tagged, newItem: MemorieResponse.Data.Memories.Memory.Tagged): Boolean {
        return oldItem == newItem
    }


}
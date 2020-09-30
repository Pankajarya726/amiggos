package com.tekzee.amiggos.ui.storieviewnew

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse


interface BannerClickListener {
    fun onItemClicked(
        position: Int,
        listItem: MemorieResponse.Data.Memories.Memory.Tagged
    )
}
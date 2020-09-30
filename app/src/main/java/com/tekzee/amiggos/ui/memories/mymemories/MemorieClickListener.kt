package com.tekzee.amiggos.ui.memories.mymemories

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface MemorieClickListener {
    fun onItemClicked(itemData: MemorieResponse.Data.Memories)
}
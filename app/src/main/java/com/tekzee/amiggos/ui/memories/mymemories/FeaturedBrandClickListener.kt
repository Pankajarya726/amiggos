package com.tekzee.amiggos.ui.memories.mymemories

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface FeaturedBrandClickListener {
    fun onItemClickedBrand(itemData: MemorieResponse.Data.Memories)
}
package com.tekzee.amiggos.ui.memories.venuefragment

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface VenueItemClickListener {
    fun onVenueItemClicked(taggedVenue: MemorieResponse.Data.Memories, adapterPosition: Int)
}
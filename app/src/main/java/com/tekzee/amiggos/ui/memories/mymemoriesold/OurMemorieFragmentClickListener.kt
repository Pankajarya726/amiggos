package com.tekzee.amiggos.ui.memories.mymemoriesold

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface OurMemorieFragmentClickListener {

    fun onMemorieClicked(storiesData: MemorieResponse.Data.Memories)
}
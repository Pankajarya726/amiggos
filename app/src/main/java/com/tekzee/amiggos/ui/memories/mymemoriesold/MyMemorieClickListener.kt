package com.tekzee.amiggos.ui.memories.mymemoriesold

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface MyMemorieClickListener {

    fun onMemorieClicked(storiesData: MemorieResponse.Data.Memories)
}
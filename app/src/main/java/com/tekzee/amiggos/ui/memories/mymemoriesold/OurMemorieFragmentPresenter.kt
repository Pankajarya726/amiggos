package com.tekzee.amiggos.ui.memories.mymemoriesold

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

class OurMemorieFragmentPresenter{

    interface MyMemoriePresenterMain{
        fun onStop()

        fun callGetOuryMemories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyMemoriePresenterMainView: BaseMainView {
        fun onMyMemorieSuccess(ourStory: List<MemorieResponse.Data.Memories>)
        fun onMyMemorieFailure(message: String)

    }
}
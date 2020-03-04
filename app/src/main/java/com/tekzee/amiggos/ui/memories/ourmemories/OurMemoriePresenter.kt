package com.tekzee.amiggos.ui.memories.ourmemories

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.mallortaxi.base.BaseMainView

class OurMemoriePresenter{

    interface OurMemoriePresenterrMain{
        fun onStop()

        fun callGetOurMemories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface OurMemoriePresenterMainView: BaseMainView {

        fun onOurMemorieSuccess(myStoryList: List<StoriesData>)
        fun onOurMemorieFailure(message: String)

    }
}
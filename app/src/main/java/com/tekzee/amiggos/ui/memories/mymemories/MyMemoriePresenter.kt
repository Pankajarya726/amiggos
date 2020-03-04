package com.tekzee.amiggos.ui.memories.mymemories

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.mallortaxi.base.BaseMainView

class MyMemoriePresenter{

    interface MyMemoriePresenterMain{
        fun onStop()

        fun callGetMyMemories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyMemoriePresenterMainView: BaseMainView {
        fun onMyMemorieSuccess(ourStory: List<StoriesData>)
        fun onMyMemorieFailure(message: String)

    }
}
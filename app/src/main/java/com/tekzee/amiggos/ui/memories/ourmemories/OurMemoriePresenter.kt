package com.tekzee.amiggos.ui.memories.ourmemories

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.memories.ourmemories.model.FeaturedBrandProductResponse
import com.tekzee.mallortaxi.base.BaseMainView

class OurMemoriePresenter{

    interface OurMemoriePresenterrMain{
        fun onStop()

        fun callGetOurMemories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallFeaturedProductFromMemory(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface OurMemoriePresenterMainView: BaseMainView {

        fun onOurMemorieSuccess(myStoryList: List<StoriesData>)
        fun onFeaturedBrandSuccess(data: List<FeaturedBrandProductResponse.Data.FeaturedProduct>)
        fun onFeaturedBrandFailure(message: String)
        fun onOurMemorieFailure(message: String)

    }
}
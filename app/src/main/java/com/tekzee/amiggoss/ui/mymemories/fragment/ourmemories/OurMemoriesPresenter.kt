package com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.mallortaxi.base.BaseMainView

class OurMemoriesPresenter {

    interface OurMemoriesMainPresenter{
        fun onStop()
        fun doCallOurMemoriesApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface OurMemoriesMainView : BaseMainView{
        fun onOurMemoriesSuccess(responseData: OurMemoriesResponse?)
        fun onOurMemoriesInfiniteSuccess(responseData: OurMemoriesResponse?)
        fun onOurMemoriesFailure(message: String)
    }

}
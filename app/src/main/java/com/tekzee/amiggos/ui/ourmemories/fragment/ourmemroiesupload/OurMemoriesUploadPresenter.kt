package com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.mallortaxi.base.BaseMainView

class OurMemoriesUploadPresenter {

    interface OurMemoriesUploadMainPresenter{
        fun onStop()
        fun doCallOurMemoriesApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface OurMemoriesUploadMainView : BaseMainView{
        fun onOurMemoriesSuccess(responseData: OurFriendListResponse?)
        fun onOurMemoriesInfiniteSuccess(responseData: OurFriendListResponse?)
        fun onOurMemoriesFailure(message: String)
    }

}
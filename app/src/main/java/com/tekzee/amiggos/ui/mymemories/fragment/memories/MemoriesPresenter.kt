package com.tekzee.amiggos.ui.mymemories.fragment.memories

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mymemories.fragment.memories.model.MyMemoriesResponse
import com.tekzee.amiggos.base.BaseMainView

class MemoriesPresenter {

    interface MemoriesMainPresenter{
        fun onStop()
        fun docallMemoriesApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface MemoriesMainView : BaseMainView {
        fun onMemoriesSuccess(responseData: MyMemoriesResponse?)
        fun onMemoriesInfiniteSuccess(responseData: MyMemoriesResponse?)
        fun onMemoriesFailure(message: String)
    }

}
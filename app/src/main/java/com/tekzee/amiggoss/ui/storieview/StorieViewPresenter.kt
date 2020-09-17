package com.tekzee.amiggoss.ui.storieview

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.mallortaxi.base.BaseMainView

class StorieViewPresenter {

    interface StorieViewMainPresenter{
        fun doCallDeleteStorie(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


        fun doAcceptOurStoryInvite(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }

    interface StorieViewMainView: BaseMainView{
        fun onDeleteStorieSuccess(responseData: CommonResponse?)
        fun onAcceptStoriesSuccess(responseData: CommonResponse?)
    }
}
package com.tekzee.amiggos.ui.storieview

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.BaseMainView

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

    interface StorieViewMainView: BaseMainView {
        fun onDeleteStorieSuccess(responseData: CommonResponse?)
        fun onAcceptStoriesSuccess(responseData: CommonResponse?)
    }
}
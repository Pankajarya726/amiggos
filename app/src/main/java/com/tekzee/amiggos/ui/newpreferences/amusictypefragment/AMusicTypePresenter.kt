package com.tekzee.amiggos.ui.newpreferences.amusictypefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import com.tekzee.amiggos.base.BaseMainView

class AMusicTypePresenter {

    interface AMusicTypePresenterMain{
        fun onStop()
        fun doCallMusicTypeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AMusicTypePresenterMainView : BaseMainView {
        fun onMusicTypeSuccess(responseData: AMusicTypeResponse?)
        fun onMusicTypeFailure(message: String)
    }

}
package com.tekzee.amiggoss.ui.newpreferences.amusictypefragment

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.newpreferences.amusictypefragment.model.AMusicTypeResponse
import com.tekzee.mallortaxi.base.BaseMainView

class AMusicTypePresenter {

    interface AMusicTypePresenterMain{
        fun onStop()
        fun doCallMusicTypeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AMusicTypePresenterMainView : BaseMainView{
        fun onMusicTypeSuccess(responseData: AMusicTypeResponse?)
        fun onMusicTypeFailure(message: String)
    }

}
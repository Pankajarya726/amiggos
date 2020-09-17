package com.tekzee.amiggoss.ui.mylifestyle

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MyLifestylePresenter {
    interface MyLifestylePresenterMain {
        fun onStop()
        fun docallMyLifestyleApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyLifestylePresenterMainView : BaseMainView {
        fun onMyLifeStyleSuccess(responseData: HomeApiResponse?)
        fun onMyLifestyleFailure(message: String)
    }
}
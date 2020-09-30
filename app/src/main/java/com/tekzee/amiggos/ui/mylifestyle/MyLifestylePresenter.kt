package com.tekzee.amiggos.ui.mylifestyle

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse
import com.tekzee.amiggos.base.BaseMainView

class MyLifestylePresenter {
    interface MyLifestylePresenterMain {
        fun onStop()
        fun docallMyLifestyleApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyLifestylePresenterMainView : BaseMainView {
        fun onMyLifeStyleSuccess(responseData: MyLifestyleResponse)
        fun onMyLifestyleFailure(message: String)
    }
}
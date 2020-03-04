package com.tekzee.amiggos.ui.signup.login_new

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.signup.login_new.model.ALoginResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ALoginPresenter {
    interface ALoginPresenterMain{
        fun onStop()
        fun doCallLoginApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface ALoginPresenterMainView: BaseMainView {
        fun OnLoginSuccess(responseData: ALoginResponse.Data)

    }
}
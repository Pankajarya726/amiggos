package com.tekzee.amiggos.ui.signup.login_new

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.signup.login_new.model.ALoginResponse
import com.tekzee.amiggos.base.BaseMainView

class ALoginPresenter {
    interface ALoginPresenterMain{
        fun onStop()
        fun doCallLoginApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doUpdateFirebaseApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface ALoginPresenterMainView: BaseMainView {
        fun OnLoginSuccess(responseData: ALoginResponse.Data)
        fun onFirebaseUpdateSuccess(responseData: LoginResponse)
    }
}
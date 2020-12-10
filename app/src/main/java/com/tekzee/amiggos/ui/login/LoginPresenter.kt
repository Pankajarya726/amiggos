package com.tekzee.amiggos.ui.login

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.base.model.CommonResponse

class LoginPresenter {
    interface LoginMainPresenter{
        fun doLoginApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doUpdateFirebaseApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }

    interface LoginMainView: BaseMainView {
        fun onLoginSuccess(responseData: LoginResponse)
        fun onFirebaseUpdateSuccess(responseData: CommonResponse?)
    }
}
package com.tekzee.amiggos.ui.login

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.mallortaxi.base.BaseMainView

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

    interface LoginMainView:BaseMainView{
        fun onLoginSuccess(responseData: LoginResponse)
        fun onFirebaseUpdateSuccess(responseData: LoginResponse)
    }
}
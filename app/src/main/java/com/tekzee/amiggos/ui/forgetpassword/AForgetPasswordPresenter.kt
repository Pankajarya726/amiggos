package com.tekzee.amiggos.ui.forgetpassword

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView

class AForgetPasswordPresenter {
    interface AForgetPasswordPresenterMain{
        fun onStop()
        fun callForgetPasswordApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface AForgetPasswordPresenterView: BaseMainView {
        fun OnForgetPasswordSuccess(responseData: String)
    }
}
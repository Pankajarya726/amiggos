package com.tekzee.amiggos.ui.signup.stepone

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData
import com.tekzee.amiggos.base.BaseMainView

class StepOnePresenter {
    interface StepOnePresenterMain{
        fun onStop()

        fun doCallSignupApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface StepOnePresenterMainView: BaseMainView {
        fun onSignupSuccess(data: UserData.Data)
        fun onSignUpFailure(message: String)
    }
}
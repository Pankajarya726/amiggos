package com.tekzee.amiggos.ui.signup.steptwo

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData
import com.tekzee.mallortaxi.base.BaseMainView

class StepTwoPresenter {
    interface StepTwoPresenterMain{
        fun onStop()

        fun doCallStateApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallCityApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


        fun doCallSignupApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface StepTwoPresenterMainView: BaseMainView {
        fun onStateSuccess(responseData: List<StateResponse.Data.States>)
        fun onSignupSuccess(data: UserData.Data)
        fun onSignUpFailure(message: String)
        fun onCitySuccess(responseData: List<CityResponse.Data.City>)
        fun onStateFailure(responseData: String)
        fun onCityFailure(responseData: String)
    }
}
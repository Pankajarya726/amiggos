package com.tekzee.amiggos.ui.signup.stepone

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.mallortaxi.base.BaseMainView

class StepOnePresenter {
    interface StepOnePresenterMain{
        fun onStop()
        fun doCallSettingsApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface StepOnePresenterMainView: BaseMainView {
        fun onSettingsSuccess(responseData: SettingsResponse?)
        fun onUpdateSettingsSuccess(responseData: UpdateSettingsResponse)
    }
}
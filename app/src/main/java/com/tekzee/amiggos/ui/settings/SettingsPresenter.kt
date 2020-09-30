package com.tekzee.amiggos.ui.settings

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.base.BaseMainView

class SettingsPresenter {

    interface SettingsMainPresenter{
        fun onStop()
        fun doCallSettingsApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doUpdateSettings(input: JsonObject, createHeaders: java.util.HashMap<String, String?>)
    }

    interface SettingsMainView: BaseMainView {
        fun onSettingsSuccess(responseData: SettingsResponse?)
        fun onUpdateSettingsSuccess(responseData: UpdateSettingsResponse)
    }
}
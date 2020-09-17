package com.tekzee.amiggoss.ui.settings

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.settings.model.SettingsResponse
import com.tekzee.amiggoss.ui.settings.model.UpdateSettingsResponse
import com.tekzee.mallortaxi.base.BaseMainView

class SettingsPresenter {

    interface SettingsMainPresenter{
        fun onStop()
        fun doCallSettingsApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doUpdateSettings(input: JsonObject, createHeaders: java.util.HashMap<String, String?>)
    }

    interface SettingsMainView: BaseMainView{
        fun onSettingsSuccess(responseData: SettingsResponse?)
        fun onUpdateSettingsSuccess(responseData: UpdateSettingsResponse)
    }
}
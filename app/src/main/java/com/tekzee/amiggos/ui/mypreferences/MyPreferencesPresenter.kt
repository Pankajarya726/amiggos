package com.tekzee.amiggos.ui.mypreferences

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceResponse
import com.tekzee.amiggos.ui.mypreferences.model.PreferenceSavedResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MyPreferencesPresenter {

    interface MyPreferencesMainPresenter {
        fun onStop()
        fun doCallGetSettings(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun docallSaveSettings(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyPreferencesMainView : BaseMainView {
        fun onGetSettingSuccess(responseData: MyPreferenceResponse?)
        fun onCallSaveSettingsSuccess(responseData: PreferenceSavedResponse?)
    }
}
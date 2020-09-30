package com.tekzee.amiggos.ui.settings_new

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.base.BaseMainView

class ASettingsPresenter {

    interface ASettingsPresenterMain{
        fun onStop()

        fun doCallUserPrfolie(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface ASettingsPresenterMainView: BaseMainView {
        fun onUserProfileSuccess(data: GetUserProfileResponse.Data)
        fun onUserProfileFailure(message: String)
    }
}
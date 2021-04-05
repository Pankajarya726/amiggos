package com.tekzee.amiggos.ui.mainsplash

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.base.BaseMainView

class MainSplashPresenter{

    interface MainSplashPresenterMain{
        fun doValidateAppVersionApi(
            input: JsonObject,
            headers: HashMap<String, String?>
        )

        fun doLanguageConstantApi(
            headers: HashMap<String, String?>,
            json: JsonObject
        )
        fun onStop();
    }

    interface MainSplashPresenterMainView: BaseMainView {
        fun onValidateAppVersionResponse(response: ValidateAppVersionResponse)
        fun onLanguageConstantResponse(asJsonObject: JsonObject)
    }
}
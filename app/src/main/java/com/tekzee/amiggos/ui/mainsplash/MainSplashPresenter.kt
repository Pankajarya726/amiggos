package com.tekzee.amiggos.ui.mainsplash

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MainSplashPresenter{

    interface MainSplashPresenterMain{
        fun doValidateAppVersionApi(
            input: JsonObject,
            headers: HashMap<String, String?>
        )

        fun doLanguageConstantApi(
            headers: HashMap<String, String?>
        )
        fun onStop();
    }

    interface MainSplashPresenterMainView: BaseMainView{
        fun onValidateAppVersionResponse(response: ValidateAppVersionResponse)
        fun onLanguageConstantResponse(response: JsonObject)
    }
}
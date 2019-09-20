package com.tekzee.amiggos.ui.chooselanguage

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.chooselanguage.model.LanguageResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ChooseLanguagePresenter {

    interface ChooseLanguagePresenterMain{
        fun doCallLanguageApi(
            createHeaders: HashMap<String, String?>
        )
        fun doLanguageConstantApi(
            headers: HashMap<String, String?>
        )
        fun onStop()
    }

    interface ChooseLanguageMainView: BaseMainView{
        fun onLanguageSuccess(responseData: LanguageResponse)
        fun onLanguageConstantResponse(response: JsonObject)
    }
}
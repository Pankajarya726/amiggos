package com.tekzee.amiggos.ui.homescreen_new.homefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.mallortaxi.base.BaseMainView

class HomePresenter {

    interface HomeMainPresenter{
        fun onStop()
        fun doCallHomeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            languageData: LanguageData?,
            callFrom: Int
        )

        fun searchApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            languageData: LanguageData?
        )
    }

    interface HomeMainView : BaseMainView {
        fun onHomeApiSuccess(responseData: HomeApiResponse?)
        fun onSearchApiSuccess(responseData: HomeApiResponse?)
        fun onHomeApiFailure(message: String)
    }

}
package com.tekzee.amiggos.ui.homescreen_new.homefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse
import com.tekzee.amiggos.ui.homescreen_new.model.BadgeCountResponse

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

        fun doCallBadgeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            languageData: LanguageData?
        )
    }

    interface HomeMainView : BaseMainView {
        fun onHomeApiSuccess(responseData: HomeResponse)
        fun onSearchApiSuccess(responseData: HomeResponse)
        fun onHomeApiFailure(message: String)
        fun onBadgeApiSuccess(responseData: BadgeCountResponse);
    }

}
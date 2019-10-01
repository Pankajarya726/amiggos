package com.tekzee.amiggos.ui.helpcenter

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.mallortaxi.base.BaseMainView

class HelpCenterPresenter {

    interface HelpCenterMainPresenter{
        fun onStop()
        fun doCallHelpCenterApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface HelpCenterMainView: BaseMainView{
        fun onHelpCenterSuccess(responseData: HelpCenterResponse?)

    }
}
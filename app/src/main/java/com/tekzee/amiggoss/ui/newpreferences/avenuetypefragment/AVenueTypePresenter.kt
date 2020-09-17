package com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import com.tekzee.mallortaxi.base.BaseMainView

class AVenueTypePresenter {

    interface AVenueTypePresenterMain{
        fun onStop()
        fun doCallVenueTypeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AVenueTypePresenterMainView : BaseMainView{
        fun onVenueTypeSuccess(responseData: AVenueTypeResponse?)
        fun onVenueTypeFailure(message: String)
    }

}
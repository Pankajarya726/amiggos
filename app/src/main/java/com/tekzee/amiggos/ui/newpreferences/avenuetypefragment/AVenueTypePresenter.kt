package com.tekzee.amiggos.ui.newpreferences.avenuetypefragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.newpreferences.avenuetypefragment.model.AVenueTypeResponse
import com.tekzee.amiggos.base.BaseMainView

class AVenueTypePresenter {

    interface AVenueTypePresenterMain{
        fun onStop()
        fun doCallVenueTypeApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface AVenueTypePresenterMainView : BaseMainView {
        fun onVenueTypeSuccess(responseData: AVenueTypeResponse?)
        fun onVenueTypeFailure(message: String)
    }

}
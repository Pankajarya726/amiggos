package com.tekzee.amiggos.ui.partydetails.fragment.pastparty

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.mallortaxi.base.BaseMainView

class PastPartyPresenter {

    interface PastPartyMainPresenter{
        fun onStop()
        fun docallPastPartyApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface PastPartyMainView : BaseMainView {
        fun onPastPartSuccess(responseData: PastPartyResponse?)
    }
}
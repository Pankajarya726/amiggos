package com.tekzee.amiggos.ui.partydetails.fragment.upcommingparty

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.base.BaseMainView

class UpcommingPartyPresenter {

    interface UpcomingPartyMainPresenter{
        fun onStop()
        fun doCallUpcomingPartyApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface UpcomingPartyMainView: BaseMainView {
        fun onUpcomingPartySuccess(responseData: PastPartyResponse)
    }
}
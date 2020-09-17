package com.tekzee.amiggoss.ui.referalcode

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.referalcode.model.ReferalCodeResponse
import com.tekzee.amiggoss.ui.referalcode.model.VenueResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ReferalCodePresenter {
    interface ReferalCodePresenterMain{

        fun doCallReferalApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun doCallCheckVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }
    interface ReferalCodeMainView: BaseMainView{
        fun onCallReferalApiSuccess(responseData: ReferalCodeResponse?)
        fun onCheckVenueSuccess(responseData: VenueResponse?)
    }
}
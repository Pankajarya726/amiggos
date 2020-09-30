package com.tekzee.amiggos.ui.mybooking

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggos.base.BaseMainView

class MyBookingPresenter {
    interface MyBookingMainPreseneter{
        fun onStop()
        fun doMyBookingApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }
    interface MyBookingMainView: BaseMainView {
        fun onMyBookingSuccess(responseData: MyBookingResponse?)
    }
}
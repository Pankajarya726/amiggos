//package com.tekzee.amiggos.ui.turningup
//
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.ui.turningup.model.TurningUpResponse
//import com.tekzee.amiggos.base.BaseMainView
//
//class TurningupPresenter  {
//
//    interface TurningUpMainPreseneter{
//        fun callTurningUpApi(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>
//        )
//        fun onStop()
//    }
//
//    interface TurningUpMainView: BaseMainView {
//        fun onTurningUpSuccess(responseData: TurningUpResponse)
//    }
//}
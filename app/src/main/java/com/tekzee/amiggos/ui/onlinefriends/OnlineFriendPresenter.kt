//package com.tekzee.amiggos.ui.onlinefriends
//
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.ui.onlinefriends.model.OnlineFriendResponse
//import com.tekzee.amiggos.base.BaseMainView
//
//class OnlineFriendPresenter {
//
//    interface OnlineFriendPresenterMain{
//        fun onStop()
//        fun doCallOnlineFriendApi(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>,
//            requestDatFromServer: Boolean
//        )
//    }
//
//    interface OnlineFriendMainView: BaseMainView {
//        fun onOnlineFriendSuccess(responseData: OnlineFriendResponse?)
//        fun onOnlineFriendInfiniteSuccess(responseData: OnlineFriendResponse?)
//        fun onOnlineFriendFailure(responseData: String)
//    }
//}
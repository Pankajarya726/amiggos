//package com.tekzee.amiggos.ui.ourmemories.fragment.nearby
//
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
//import com.tekzee.amiggos.base.BaseMainView
//
//class NearByPresenter {
//
//    interface NearByMainPresenter{
//        fun onStop()
//        fun doCallNearBy(
//            input: JsonObject,
//            createHeaders: HashMap<String, String?>,
//            requestDatFromServer: Boolean
//        )
//    }
//
//    interface NearByMainView : BaseMainView {
//        fun onOurMemoriesSuccess(responseData: OurFriendListResponse?)
//        fun onOurMemoriesInfiniteSuccess(responseData: OurFriendListResponse?)
//        fun onOurMemoriesFailure(message: String)
//    }
//
//}
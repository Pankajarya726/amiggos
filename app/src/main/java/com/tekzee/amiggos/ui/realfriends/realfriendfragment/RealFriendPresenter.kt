package com.tekzee.amiggos.ui.realfriends.realfriendfragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendV2Response
import com.tekzee.amiggos.base.BaseMainView

class RealFriendPresenter {

    interface RealFriendMainPresenter{
        fun onStop()
        fun doCallRealFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean,
            fragmentVisible: Boolean
        )
    }

    interface RealFriendMainView : BaseMainView {
        fun onRealFriendSuccess(
            responseData: List<RealFriendV2Response.Data.RealFreind>,
            totalCount: Int,
            responseData1: RealFriendV2Response
        )
        fun onRealFriendInfiniteSuccess(
            responseData: List<RealFriendV2Response.Data.RealFreind>,
            responseData1: RealFriendV2Response
        )
        fun onRealFriendFailure(message: String)
    }

}
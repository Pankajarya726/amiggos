package com.tekzee.amiggos.ui.realfriends.realfriendfragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.model.RealFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class RealFriendPresenter {

    interface RealFriendMainPresenter{
        fun onStop()
        fun doCallRealFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface RealFriendMainView : BaseMainView{
        fun onRealFriendSuccess(responseData: RealFriendResponse?)
        fun onRealFriendInfiniteSuccess(responseData: RealFriendResponse?)
        fun onRealFriendFailure(message: String)
    }

}
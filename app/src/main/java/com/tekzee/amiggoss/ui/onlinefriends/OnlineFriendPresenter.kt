package com.tekzee.amiggoss.ui.onlinefriends

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.onlinefriends.model.OnlineFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class OnlineFriendPresenter {

    interface OnlineFriendPresenterMain{
        fun onStop()
        fun doCallOnlineFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface OnlineFriendMainView: BaseMainView{
        fun onOnlineFriendSuccess(responseData: OnlineFriendResponse?)
        fun onOnlineFriendInfiniteSuccess(responseData: OnlineFriendResponse?)
        fun onOnlineFriendFailure(responseData: String)
    }
}
package com.tekzee.amiggoss.ui.viewfriends

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ViewFriendPresenter {
    interface ViewFriendMainPresenter{

        fun onStop()

        fun docallViewFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface ViewFriendMainView: BaseMainView{
        fun onViewFriendSuccess(responseData: StorieViewResponse?)
    }
}
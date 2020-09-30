package com.tekzee.amiggos.ui.viewfriends

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import com.tekzee.amiggos.base.BaseMainView

class ViewFriendPresenter {
    interface ViewFriendMainPresenter{

        fun onStop()

        fun docallViewFriendApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


    }

    interface ViewFriendMainView: BaseMainView {
        fun onViewFriendSuccess(responseData: StorieViewResponse?)
    }
}
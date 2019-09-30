package com.tekzee.amiggos.ui.invitefriend

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.UpdateFriendCountResponse
import com.tekzee.mallortaxi.base.BaseMainView

class InviteFriendActivityPresenter {

    interface InviteFriendPresenterMain{

        fun onStop()
        fun doUpdateFriendCount(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface InviteFriendMainView: BaseMainView{
        fun onUpdateFriendCountSuccess(responseData: UpdateFriendCountResponse?)
    }
}
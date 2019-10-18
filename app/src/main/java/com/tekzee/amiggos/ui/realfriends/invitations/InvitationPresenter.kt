package com.tekzee.amiggos.ui.realfriends.invitations

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class InvitationPresenter {

    interface InvitationMainPresenter{
        fun onStop()
        fun doCallInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doAcceptInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doRejectInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface InvitationMainView : BaseMainView{
        fun onInvitaionSuccess(responseData: InvitationResponse?)
        fun onInvitationFailure(responseData: String)
        fun onAcceptInvitation(responseData: CommonResponse?)
        fun onRejectInvitation(responseData: CommonResponse?)

    }
}
package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.amiggos.base.BaseMainView

class PartyInvitesPresenter {

    interface PartyInviteMainPresenter{
        fun onStop()
        fun doCallPartyInviteApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallJoinPartyInvites(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallDeclinePartyInvites(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface PartyInviteMainView : BaseMainView {
        fun onPartyInviteSuccess(responseData: PartyInvitesResponse?)
        fun onJoinPartyInvitesSuccess(responseData: CommonResponse?)
        fun onDeclinePartyInvitesSuccess(responseData: CommonResponse?)
    }
}
package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.mallortaxi.base.BaseMainView

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

    interface PartyInviteMainView : BaseMainView{
        fun onPartyInviteSuccess(responseData: PartyInvitesResponse?)
        fun onJoinPartyInvitesSuccess(responseData: PartyInvitesResponse?)
        fun onDeclinePartyInvitesSuccess(responseData: PartyInvitesResponse?)
    }
}
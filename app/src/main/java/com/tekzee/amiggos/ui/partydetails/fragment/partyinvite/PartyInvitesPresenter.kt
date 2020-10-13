package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse

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
        fun onPartyInviteSuccess(responseData: BookingInvitationResponse?)
        fun onJoinPartyInvitesSuccess(responseData: CommonResponse?)
        fun onDeclinePartyInvitesSuccess(responseData: CommonResponse?)
    }
}
package com.tekzee.amiggos.ui.notification_new.fragments.partyinvites

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.notification_new.model.PartyInvitesNotificationResponse

class PartyInvitesFragmentPresenter {

    interface PartyInvitesFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
        fun callAcceptPartyInviteApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun callRejectPartyInviteApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface PartyInvitesFragmentPresenterMainView: BaseMainView {
        fun onNotificationSuccess(responseData: List<PartyInvitesNotificationResponse.Data.UserNotification>)
        fun onNotificationInfiniteSuccess(responseData: List<PartyInvitesNotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
        fun onAcceptPartyRequestSuccess(responseData: String)
        fun onRejectPartyRequestSuccess(responseData: String)
    }
}
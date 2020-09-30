package com.tekzee.amiggos.ui.notification_new.fragments.partyinvites

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.base.BaseMainView

class PartyInvitesFragmentPresenter {

    interface PartyInvitesFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface PartyInvitesFragmentPresenterMainView: BaseMainView {
        fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationInfiniteSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
    }
}
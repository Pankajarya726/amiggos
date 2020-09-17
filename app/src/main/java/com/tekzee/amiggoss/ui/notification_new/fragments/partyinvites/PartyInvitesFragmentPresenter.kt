package com.tekzee.amiggoss.ui.notification_new.fragments.partyinvites

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.notification_new.model.ANotificationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class PartyInvitesFragmentPresenter {

    interface PartyInvitesFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface PartyInvitesFragmentPresenterMainView: BaseMainView{
        fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
    }
}
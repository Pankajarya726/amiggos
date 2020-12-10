package com.tekzee.amiggos.ui.notification_new.fragments.memoriesnotification

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.base.BaseMainView

class MemorieNotificationFragmentPresenter {

    interface MemorieNotificationFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
        fun doCallRejectMemoryInvite(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MemorieNotificationFragmentPresenterMainView: BaseMainView {
        fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationInfiniteSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
        fun onMemoryInviteRejected(message: String)
    }
}
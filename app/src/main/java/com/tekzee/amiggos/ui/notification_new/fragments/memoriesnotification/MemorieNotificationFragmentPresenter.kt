package com.tekzee.amiggos.ui.notification_new.fragments.memoriesnotification

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MemorieNotificationFragmentPresenter {

    interface MemorieNotificationFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MemorieNotificationFragmentPresenterMainView: BaseMainView{
        fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
    }
}
package com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class FriendRequestFragmentPresenter {

    interface FriendRequestFragmentPresenterMain{
        fun onStop()
        fun doCallNotificationOne(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface FriendRequestFragmentPresenterMainView: BaseMainView{
        fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>)
        fun onNotificationFailure(responseData: String)
    }
}
package com.tekzee.amiggos.ui.notification

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.notification.model.NotificationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class NotificationPresenter {

    interface NotificationMainPresenter{
        fun onStop()
        fun doCallGetNotification(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )

        fun doCallClearNotification(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface NotificationMainView: BaseMainView{
        fun onGetNotificationSuccess(responseData: NotificationResponse?)
        fun onNotificationInfiniteSuccess(responseData: NotificationResponse?)
        fun onNotificationFailure(responseData: String)
        fun onClearNotificationSuccess(commonResponse: CommonResponse)
    }
}
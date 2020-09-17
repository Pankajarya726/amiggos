package com.tekzee.amiggoss.ui.notification_new.model


import com.google.gson.annotations.SerializedName

data class ANotificationResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("user_notification")
        var userNotification: List<UserNotification> = listOf()
    ) {
        data class UserNotification(
            @SerializedName("data")
            var `data`: String = "",
            @SerializedName("device_type")
            var deviceType: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("is_read")
            var isRead: Int = 0,
            @SerializedName("message")
            var message: String = "",
            @SerializedName("notification_key")
            var notificationKey: Int = 0,
            @SerializedName("subject")
            var subject: String = ""
        )
    }
}
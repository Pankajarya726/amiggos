package com.tekzee.amiggos.ui.notification_new.model


import com.google.gson.annotations.SerializedName

data class PartyInvitesNotificationResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("user_notification")
        val userNotification: List<UserNotification> = listOf()
    ) {
        data class UserNotification(
            @SerializedName("data")
            val `data`: Data = Data(),
            @SerializedName("device_type")
            val deviceType: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("is_read")
            val isRead: Int = 0,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("notification_key")
            val notificationKey: Int = 0,
            @SerializedName("subject")
            val subject: String = "",
            val loadingStatus:Boolean = false
        ) {
            data class Data(
                @SerializedName("booking_id")
                val bookingId: String = "",
                @SerializedName("notification_key")
                val notificationKey: Int = 0,
                @SerializedName("owner_id")
                val ownerId: Int = 0,
                @SerializedName("user_id")
                val userId: Any? = null
            )
        }
    }
}
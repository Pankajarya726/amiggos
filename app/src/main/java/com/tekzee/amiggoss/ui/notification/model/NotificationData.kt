package com.tekzee.amiggoss.ui.notification.model


import com.google.gson.annotations.SerializedName

data class NotificationData(
    var loadingStatus: Boolean = false,
    @SerializedName("data")
    val `data`: String = "",
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
    val subject: String = ""
)
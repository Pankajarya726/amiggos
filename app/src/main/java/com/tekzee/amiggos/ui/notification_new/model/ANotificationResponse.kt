package com.tekzee.amiggos.ui.notification_new.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import java.io.Serializable

data class ANotificationResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("user_notification")
        val userNotification: List<UserNotification> = listOf()
    ):Serializable {
        data class UserNotification(
            @SerializedName("data")
            val `data`: Data = Data(),
            @SerializedName("device_type")
            val deviceType: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("memories_list")
            val memoriesList: List<MemorieResponse.Data.Memories> = listOf(),
            @SerializedName("is_read")
            val isRead: Int = 0,
            @SerializedName("message")
            val message: String = "",
            @SerializedName("notification_key")
            val notificationKey: Int = 0,
            @SerializedName("subject")
            val subject: String = "",
            var loadingStatus: Boolean = false
        ):Serializable {
            data class Data(
                @SerializedName("booking_id")
                val bookingId: String = "",
                @SerializedName("notification_key")
                val notificationKey: Int = 0,
                @SerializedName("owner_id")
                val ownerId: Int = 0,
                @SerializedName("userid")
                val userId: Int? = 0,
                @SerializedName("creator_id")
                val creator_id: Int = 0,
                @SerializedName("sender_id")
                val sender_id: Int = 0,
                @SerializedName("user_type")
                val user_type: Int = 0,
                @SerializedName("our_story_id")
                val our_story_id: String = "0",
                @SerializedName("friend_id")
                val friendId: String = ""
            ):Serializable
        }
    }
}
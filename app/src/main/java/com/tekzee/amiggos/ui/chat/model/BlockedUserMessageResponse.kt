package com.tekzee.amiggos.ui.chat.model


import com.google.gson.annotations.SerializedName

data class BlockedUserMessageResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("blocked_user_message")
        val blockedUserMessage: String = "",
        @SerializedName("is_message_blocked")
        val isMessageBlocked: Int = 0,
        @SerializedName("is_user_blocked")
        val isUserBlocked: Int = 0,
        @SerializedName("message_blocked_message")
        val messageBlockedMessage: String = ""
    )
}
package com.tekzee.amiggos.ui.chatnew.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatMessage(
    @SerializedName("isSeen")
    var isSeen: Boolean = false,
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("receiver")
    val `receiver`: String = "",
    @SerializedName("sender")
    val sender: String = "",
    @SerializedName("senderImage")
    val senderImage: String = "",
    @SerializedName("SenderName")
    val SenderName: String = "",
    @SerializedName("friendName")
    val friendName: String = "",
    @SerializedName("friendImage")
    val friendImage: String = "",
    @SerializedName("message_id")
    val message_id: String = "",
    @SerializedName("roomid")
    val roomid: String = "",
    @SerializedName("timestamp")
    val timeSpam: Long = 0
):Serializable
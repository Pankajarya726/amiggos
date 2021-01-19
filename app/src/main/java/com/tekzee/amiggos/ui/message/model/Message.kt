package com.tekzee.amiggos.ui.message.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Message(
    @SerializedName("seen")
    var isSeen: Boolean = false,
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("receiver")
    val `receiver`: String = "",
    @SerializedName("sender")
    val sender: String = "",
    @SerializedName("message_id")
    val message_id: String = "",
    @SerializedName("timestamp")
    val timeSpam: Long = 0
):Serializable
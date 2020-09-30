package com.tekzee.amiggos.ui.chat.model


import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("isSeen")
    var isSeen: Boolean = false,
    @SerializedName("msg")
    val msg: String = "",
    @SerializedName("receiver")
    val `receiver`: String = "",
    @SerializedName("sender")
    val sender: String = "",
    @SerializedName("timeSpam")
    val timeSpam: Long = 0
)
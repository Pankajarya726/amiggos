package com.tekzee.amiggos.ui.message.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyConversation(
    @SerializedName("conversationid")
    var conversationid: String? = "",
    @SerializedName("receiverid")
    var receiverid: String? = "",
    @SerializedName("message")
    var message: String? = "",
    @SerializedName("recevieramiggosid")
    var recevieramiggosid: String? = "",
    @SerializedName("timestamp")
    var timestamp: Long?=0L,
    @SerializedName("senderamiggosid")
    var senderamiggosid: String? = "",
    @SerializedName("image")
    var image: String? = "",
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("senderid")
    var senderid: String? = ""
):Serializable


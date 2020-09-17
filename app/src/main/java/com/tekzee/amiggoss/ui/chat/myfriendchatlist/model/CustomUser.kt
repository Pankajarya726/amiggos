package com.tekzee.amiggoss.ui.chat.myfriendchatlist.model


import com.google.gson.annotations.SerializedName

data class CustomUser(
    @SerializedName("amiggosId")
    val amiggosId: String = "",
    @SerializedName("firebaseuserid")
    val firebaseuserid: String = ""
)
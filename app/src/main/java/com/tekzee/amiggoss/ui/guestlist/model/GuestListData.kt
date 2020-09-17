package com.tekzee.amiggoss.ui.guestlist.model


import com.google.gson.annotations.SerializedName

data class GuestListData(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("userid")
    val userid: Int = 0
)
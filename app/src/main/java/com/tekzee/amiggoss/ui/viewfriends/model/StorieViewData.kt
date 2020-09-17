package com.tekzee.amiggoss.ui.viewfriends.model


import com.google.gson.annotations.SerializedName

data class StorieViewData(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("userid")
    val userid: Int = 0
)
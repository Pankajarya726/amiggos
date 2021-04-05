package com.tekzee.amiggos.ui.viewfriends.model


import com.google.gson.annotations.SerializedName

data class StorieViewData(
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("profile")
    val profile: String = "",
    @SerializedName("user_id")
    val userid: Int = 0
)
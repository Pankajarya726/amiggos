package com.tekzee.amiggos.ui.invitationlist.model


import com.google.gson.annotations.SerializedName

data class InvitationListResponse(
    @SerializedName("data")
    val `data`: ArrayList<Data> = ArrayList(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("is_checkin")
        val isCheckin: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("profile")
        val profile: String = "",
        @SerializedName("userid")
        val userid: Int = 0,
        @SerializedName("checkedin_time")
        val checkedin_time: String = ""
    )
}
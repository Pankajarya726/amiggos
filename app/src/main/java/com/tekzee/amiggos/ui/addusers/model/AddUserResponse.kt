package com.tekzee.amiggosvenueapp.ui.addusers.model


import com.google.gson.annotations.SerializedName

data class AddUserResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("guest_list")
        val staffList: ArrayList<Staff> = ArrayList()
    ) {
        data class Staff(
            @SerializedName("first_name")
            val firstName: String = "",
            @SerializedName("userid")
            val id: Int = 0,
            @SerializedName("last_name")
            val lastName: String = "",
            @SerializedName("profile_image")
            val profileImage: String = "",
            @SerializedName("status")
            val status: Int = 0
        )
    }
}
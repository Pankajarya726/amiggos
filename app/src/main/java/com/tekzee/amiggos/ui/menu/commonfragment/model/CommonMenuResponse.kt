package com.tekzee.amiggos.ui.menu.commonfragment.model


import com.google.gson.annotations.SerializedName

data class CommonMenuResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("staff_list")
        val staffList: ArrayList<Staff> = ArrayList()
    ) {
        data class Staff(
            @SerializedName("first_name")
            val firstName: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("last_name")
            val lastName: String = "",
            @SerializedName("status")
            val isActive: Int = 0,
            @SerializedName("profile_image")
            val profileImage: String = ""
        )
    }
}
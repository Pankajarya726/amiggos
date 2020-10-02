package com.tekzee.amiggos.ui.menu.model


import com.google.gson.annotations.SerializedName

data class MenuResponse(
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
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = ""
        )
    }
}
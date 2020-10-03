package com.tekzee.amiggos.ui.venuedetailsnew.model


import com.google.gson.annotations.SerializedName

data class VenueDetails(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("club_data")
        val clubData: ClubData = ClubData()
    ) {
        data class ClubData(
            @SerializedName("address")
            val address: String = "",
            @SerializedName("agelimit")
            val agelimit: String = "",
            @SerializedName("club_city")
            val clubCity: String = "",
            @SerializedName("club_country")
            val clubCountry: Any? = null,
            @SerializedName("club_description")
            val clubDescription: String = "",
            @SerializedName("club_id")
            val clubId: Int = 0,
            @SerializedName("club_state")
            val clubState: String = "",
            @SerializedName("club_working_value")
            val clubWorkingValue: String = "",
            @SerializedName("dress_code")
            val dressCode: String = "",
            @SerializedName("go_order")
            val goOrder: Int = 0,
            @SerializedName("home_image")
            val homeImage: List<String> = listOf(),
            @SerializedName("is_favorite")
            val isFavorite: Int = 0,
            @SerializedName("latitude")
            val latitude: String = "",
            @SerializedName("longitude")
            val longitude: String = "",
            @SerializedName("mask_req")
            val maskReq: Int = 0,
            @SerializedName("menu_type")
            val menuType: Int = 0,
            @SerializedName("menu_type_name")
            val menuTypeName: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("phone_number")
            val phoneNumber: String = "",
            @SerializedName("price_category")
            val priceCategory: String = "",
            @SerializedName("reservation")
            val reservation: Int = 0,
            @SerializedName("tax")
            val tax: Int = 0,
            @SerializedName("teamadmin_id")
            val teamadminId: Int = 0,
            @SerializedName("user_set_time")
            val userSetTime: Int = 0,
            @SerializedName("working_days")
            val workingDays: List<WorkingDay> = listOf(),
            @SerializedName("zipcode")
            val zipcode: String = "",
            @SerializedName("mask_image")
            val maskimage: String = ""
        ) {
            data class WorkingDay(
                @SerializedName("is_open")
                val isOpen: Int = 0,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("timing")
                val timing: String = ""
            )
        }
    }
}
package com.tekzee.amiggos.ui.venuedetails


import com.google.gson.annotations.SerializedName

data class vne(
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
            @SerializedName("calendar")
            val calendar: Calendar = Calendar(),
            @SerializedName("club_city")
            val clubCity: String = "",
            @SerializedName("club_country")
            val clubCountry: String = "",
            @SerializedName("club_description")
            val clubDescription: String = "",
            @SerializedName("club_id")
            val clubId: Int = 0,
            @SerializedName("club_state")
            val clubState: String = "",
            @SerializedName("club_working_value")
            val clubWorkingValue: String = "",
            @SerializedName("dateonly")
            val dateonly: String = "",
            @SerializedName("dress_code")
            val dressCode: String = "",
            @SerializedName("go_order")
            val goOrder: Int = 0,
            @SerializedName("home_image")
            val homeImage: List<String> = listOf(),
            @SerializedName("is_amigo_club")
            val isAmigoClub: Int = 0,
            @SerializedName("is_booking_available")
            val isBookingAvailable: String = "",
            @SerializedName("is_favorite")
            val isFavorite: Int = 0,
            @SerializedName("isclock")
            val isclock: String = "",
            @SerializedName("latitude")
            val latitude: String = "",
            @SerializedName("longitude")
            val longitude: String = "",
            @SerializedName("mask_image")
            val maskImage: String = "",
            @SerializedName("mask_req")
            val maskReq: Int = 0,
            @SerializedName("menu_type")
            val menuType: Int = 0,
            @SerializedName("menu_type_name")
            val menuTypeName: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("other_img")
            val otherImg: String = "",
            @SerializedName("phone")
            val phone: String = "",
            @SerializedName("price_category")
            val priceCategory: String = "",
            @SerializedName("reservation")
            val reservation: Int = 0,
            @SerializedName("tax")
            val tax: Int = 0,
            @SerializedName("teamadmin_id")
            val teamadminId: Int = 0,
            @SerializedName("timeslot")
            val timeslot: String = "",
            @SerializedName("user_set_time")
            val userSetTime: Int = 0,
            @SerializedName("working_days")
            val workingDays: List<WorkingDay> = listOf(),
            @SerializedName("zipcode")
            val zipcode: String = ""
        ) {
            data class Calendar(
                @SerializedName("fri")
                val fri: Int = 0,
                @SerializedName("mon")
                val mon: Int = 0,
                @SerializedName("sat")
                val sat: Int = 0,
                @SerializedName("sun")
                val sun: Int = 0,
                @SerializedName("thu")
                val thu: Int = 0,
                @SerializedName("tue")
                val tue: Int = 0,
                @SerializedName("wed")
                val wed: Int = 0
            )

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
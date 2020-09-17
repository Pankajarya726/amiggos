package com.tekzee.amiggoss.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class PackageData(
    @SerializedName("club_id")
    val clubId: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("is_standard")
    val isStandard: Int = 0,
    @SerializedName("no_seat")
    val noSeat: String = "",
    @SerializedName("package_date")
    val packageDate: String = "",
    @SerializedName("package_id")
    val packageId: Int = 0,
    @SerializedName("package_image")
    val packageImage: String = "",
    @SerializedName("package_name")
    val packageName: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("start_time")
    val startTime: String = "",
    @SerializedName("symbol_left")
    val symbolLeft: String = "",
    @SerializedName("symbol_right")
    val symbolRight: String = "",
    @SerializedName("table_quantity")
    val tableQuantity: Int = 0,
    @SerializedName("table_type")
    val tableType: Int = 0
)
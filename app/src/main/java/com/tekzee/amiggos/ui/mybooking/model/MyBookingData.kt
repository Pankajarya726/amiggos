package com.tekzee.amiggos.ui.mybooking.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyBookingData(
    @SerializedName("booking_code")
    val bookingCode: String = "",
    @SerializedName("club_id")
    val clubId: Int = 0,
    @SerializedName("club_name")
    val clubName: String = "",
    @SerializedName("date_time")
    val dateTime: String = "",
    @SerializedName("end_time")
    val endTime: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("package_id")
    val packageId: Int = 0,
    @SerializedName("party_date")
    val partyDate: String = "",
    @SerializedName("price")
    val price: Int = 0,
    @SerializedName("qr_code")
    val qrCode: String = "",
    @SerializedName("start_time")
    val startTime: String = "",
    @SerializedName("symbol_left")
    val symbolLeft: String = "",
    @SerializedName("symbol_right")
    val symbolRight: String = "",
    @SerializedName("user_id")
    val userId: Int = 0
): Serializable
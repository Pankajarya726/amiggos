package com.tekzee.amiggoss.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class PackageBookData(
    @SerializedName("booking_id")
    val bookingId: String = "",
    @SerializedName("amount")
    val amount: String = ""
)
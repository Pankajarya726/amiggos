package com.tekzee.amiggos.ui.homescreen_new.model


import com.google.gson.annotations.SerializedName

data class BadgeCountResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("booking_count_batch")
        val bookingCountBatch: String = "",
        @SerializedName("memory_count_batch")
        val memoryCountBatch: String = "",
        @SerializedName("near_by_count_batch")
        val nearByCountBatch: String = "",
        @SerializedName("notification_count_batch")
        val notificationCountBatch: String = ""
    )
}
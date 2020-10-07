package com.tekzee.amiggos.ui.bookingdetailnew.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookingDetailsNewResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("booking")
        val booking: Booking = Booking()
    ):Serializable{
        data class Booking(
            @SerializedName("address")
            val address: String = "",
            @SerializedName("booking_code")
            val bookingCode: String = "",
            @SerializedName("booking_date")
            val bookingDate: String = "",
            @SerializedName("booking_id")
            val bookingId: Int = 0,
            @SerializedName("booking_method")
            val bookingMethod: String = "",
            @SerializedName("booking_time")
            val bookingTime: String = "",
            @SerializedName("currency")
            val currency: String = "",
            @SerializedName("description")
            val description: String = "",
            @SerializedName("instructions")
            val instructions: String = "",
            @SerializedName("last_name")
            val lastName: String = "",
            @SerializedName("menus")
            val menus: List<Menu> = listOf(),
            @SerializedName("name")
            val name: String = "",
            @SerializedName("purchased_by")
            val purchasedBy: String = "",
            @SerializedName("qr_code")
            val qrCode: String = "",
            @SerializedName("total_amount")
            val totalAmount: Int = 0,
            @SerializedName("totalInvited_guest")
            val totalInvitedGuest: Int = 0,
            @SerializedName("userid")
            val userid: Int = 0,
            @SerializedName("venue_home_image")
            val venueHomeImage: String = "",
            @SerializedName("venue_id")
            val venueId: Int = 0
        ):Serializable {
            data class Menu(
                @SerializedName("description")
                val description: String = "",
                @SerializedName("id")
                val id: Int = 0,
                @SerializedName("menu_image")
                val menuImage: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("price")
                val price: Int = 0,
                @SerializedName("qty")
                val qty: Int = 0
            ):Serializable
        }
    }
}
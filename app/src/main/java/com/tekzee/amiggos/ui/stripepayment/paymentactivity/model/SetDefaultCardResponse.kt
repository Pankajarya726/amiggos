package com.tekzee.amiggos.ui.stripepayment.paymentactivity.model


import com.google.gson.annotations.SerializedName

data class SetDefaultCardResponse(
    @SerializedName("customer_stripId")
    val customerStripId: String = "",
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false
) {
    data class Data(
        @SerializedName("cards")
        val cards: List<Card> = listOf()
    ) {
        data class Card(
            @SerializedName("brand")
            val brand: String = "",
            @SerializedName("country")
            val country: String = "",
            @SerializedName("exp_month")
            val expMonth: Int = 0,
            @SerializedName("exp_year")
            val expYear: Int = 0,
            @SerializedName("finger_print")
            val fingerPrint: String = "",
            @SerializedName("icon")
            val icon: String = "",
            @SerializedName("id")
            val id: String = "",
            @SerializedName("is_default")
            val isDefault: Int = 0,
            @SerializedName("last4")
            val last4: String = ""
        )
    }
}
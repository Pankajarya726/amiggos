package com.tekzee.amiggos.ui.stripepayment.model


import com.google.gson.annotations.SerializedName

data class CardListResponse(
    @SerializedName("customer_stripId")
    var customerStripId: String = "",
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("success")
    var success: Boolean = false
) {
    data class Data(
        @SerializedName("cards")
        var cards: List<Card> = listOf()
    ) {
        data class Card(
            @SerializedName("brand")
            var brand: String = "",
            @SerializedName("country")
            var country: String = "",
            @SerializedName("exp_month")
            var expMonth: Int = 0,
            @SerializedName("exp_year")
            var expYear: Int = 0,
            @SerializedName("finger_print")
            var fingerPrint: String = "",
            @SerializedName("id")
            var id: String = "",
            @SerializedName("is_default")
            var isDefault: Int = 0,
            @SerializedName("last4")
            var last4: String = "",
            var isSelected: Boolean = false
        )
    }
}
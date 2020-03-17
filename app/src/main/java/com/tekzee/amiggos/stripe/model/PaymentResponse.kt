package com.tekzee.amiggos.stripe.model


import com.google.gson.annotations.SerializedName

data class PaymentResponse(
    @SerializedName("amount")
    var amount: Int = 0,
    @SerializedName("canceledAt")
    var canceledAt: Int = 0,
    @SerializedName("captureMethod")
    var captureMethod: String = "",
    @SerializedName("clientSecret")
    var clientSecret: String = "",
    @SerializedName("confirmationMethod")
    var confirmationMethod: String = "",
    @SerializedName("created")
    var created: Int = 0,
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("isLiveMode")
    var isLiveMode: Boolean = false,
    @SerializedName("objectType")
    var objectType: String = "",
    @SerializedName("paymentMethodTypes")
    var paymentMethodTypes: List<String> = listOf(),
    @SerializedName("status")
    var status: String = ""
)
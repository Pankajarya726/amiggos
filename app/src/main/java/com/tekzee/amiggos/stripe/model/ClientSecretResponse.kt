package com.tekzee.amiggos.stripe.model


import com.google.gson.annotations.SerializedName

data class ClientSecretResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("booking_id")
        var bookingId: String = "",
        @SerializedName("clientSecret")
        var clientSecret: String = "",
        @SerializedName("publishableKey")
        var publishableKey: String = ""
    )
}
package com.tekzee.amiggos.ui.signup.steptwo.model


import com.google.gson.annotations.SerializedName

data class StateResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("states_list")
        var statesList: List<States> = listOf()
    ) {
        data class States(
            @SerializedName("country_id")
            var countryId: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("state_code")
            var stateCode: String = ""
        )
    }
}
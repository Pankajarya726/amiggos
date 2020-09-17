package com.tekzee.amiggoss.ui.signup.steptwo.model


import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("city_list")
        var cityList: List<City> = listOf()
    ) {
        data class City(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("state_id")
            var stateId: Int = 0
        )
    }
}
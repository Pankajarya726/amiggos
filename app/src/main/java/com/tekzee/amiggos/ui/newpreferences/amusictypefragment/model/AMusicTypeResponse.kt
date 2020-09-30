package com.tekzee.amiggos.ui.newpreferences.amusictypefragment.model


import com.google.gson.annotations.SerializedName

data class AMusicTypeResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("music_type")
        var musicType: List<MusicType> = listOf()
    ) {
        data class MusicType(
            @SerializedName("mid")
            var mid: Int = 0,
            @SerializedName("music")
            var music: String = ""
        )
    }
}
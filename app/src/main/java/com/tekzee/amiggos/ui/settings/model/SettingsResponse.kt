package com.tekzee.amiggos.ui.settings.model


import com.google.gson.annotations.SerializedName

data class SettingsResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("setting")
        val setting: List<Setting> = listOf()
    ) {
        data class Setting(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("is_set")
            val isSet: Int = 0,
            @SerializedName("title")
            val title: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("url")
            val url: String = ""
        )
    }
}
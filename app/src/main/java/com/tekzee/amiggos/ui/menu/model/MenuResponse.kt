package com.tekzee.amiggos.ui.menu.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.room.entity.Menu
import java.io.Serializable
import java.security.SecureRandom

data class MenuResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
):Serializable {
    data class Data(
        @SerializedName("section")
        val section: List<Section> = listOf(),
        @SerializedName("tax")
        val tax: Tax = Tax()
    ):Serializable {
        data class Section(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("menu")
            val menu: List<Menu> = listOf(),
            @SerializedName("name")
            val name: String = ""
        ):Serializable
    } data class Tax(
        @SerializedName("currency")
        val currency: String = "",
        @SerializedName("key")
        val key: String = "",
        @SerializedName("value")
        val value: String = ""
    ):Serializable
}
package com.tekzee.amiggos.ui.mylifestyle.model


import com.google.gson.annotations.SerializedName
import com.tekzee.amiggos.ui.mylifestyle.AMyLifeStyle

data class MyLifestyleResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("lifestyle")
        val lifestyle: List<Lifestyle> = listOf()
    ) {
        data class Lifestyle(
            @SerializedName("cat_image")
            val catImage: String = "",
            @SerializedName("children")
            val children: String = "",
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("selected_subCategory")
            val selectedSubCategory: Int = 0,
            @SerializedName("children_count")
            val children_count: Int = 0
        )
    }
}
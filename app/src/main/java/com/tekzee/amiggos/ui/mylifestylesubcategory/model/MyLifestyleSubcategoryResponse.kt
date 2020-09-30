package com.tekzee.amiggos.ui.mylifestylesubcategory.model


import com.google.gson.annotations.SerializedName

data class MyLifestyleSubcategoryResponse(
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
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("parent_id")
            val parentId: Int = 0,
            @SerializedName("selected")
            var selected: Int = 0
        )
    }
}
package com.tekzee.amiggosvenueapp.ui.tagging.model


import com.google.gson.annotations.SerializedName

data class TaggingResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
) {
    data class Data(
        @SerializedName("search")
        val search: ArrayList<Search> = ArrayList()
    ) {
        data class Search(
            @SerializedName("id")
            val id: Int = 0,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("image")
            val image: String = ""
        )
    }
}
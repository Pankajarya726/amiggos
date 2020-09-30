package com.tekzee.amiggos.ui.memories.ourmemories.model


import com.google.gson.annotations.SerializedName

data class FeaturedBrandProductResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("memories_list")
        var featuredProduct: List<FeaturedProduct> = listOf()
    ) {
        data class FeaturedProduct(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("venue_id")
            var venue_id: Int = 0,
            @SerializedName("name")
            var name: String = "",
            @SerializedName("profile")
            var profile: String = ""
        )
    }
}
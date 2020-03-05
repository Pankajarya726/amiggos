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
        @SerializedName("featured_product")
        var featuredProduct: List<FeaturedProduct> = listOf()
    ) {
        data class FeaturedProduct(
            @SerializedName("product_id")
            var productId: Int = 0,
            @SerializedName("product_image")
            var productImage: String = ""
        )
    }
}
package com.tekzee.amiggos.ui.memories.ourmemories.model


import com.google.gson.annotations.SerializedName

data class GetOurMemoriesResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("featured_product")
        var featuredProduct: List<FeaturedProduct> = listOf(),
        @SerializedName("our_story")
        var ourStory: List<OurStory> = listOf()
    ) {
        data class FeaturedProduct(
            @SerializedName("product_id")
            var productId: Int = 0,
            @SerializedName("product_image")
            var productImage: String = ""
        )

        data class OurStory(
            @SerializedName("content")
            var content: List<Content> = listOf(),
            @SerializedName("created_at")
            var createdAt: String = "",
            @SerializedName("imageUrl")
            var imageUrl: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("our_story_id")
            var ourStoryId: Int = 0,
            @SerializedName("userid")
            var userid: Int = 0
        ) {
            data class Content(
                @SerializedName("api_url")
                var apiUrl: String = "",
                @SerializedName("created_at")
                var createdAt: String = "",
                @SerializedName("id")
                var id: Int = 0,
                @SerializedName("is_user_file")
                var isUserFile: Int = 0,
                @SerializedName("name")
                var name: String = "",
                @SerializedName("profile")
                var profile: String = "",
                @SerializedName("type")
                var type: String = "",
                @SerializedName("url")
                var url: String = "",
                @SerializedName("user_id")
                var userId: Int = 0,
                @SerializedName("video_thumb")
                var videoThumb: String = "",
                @SerializedName("viewCount")
                var viewCount: Int = 0
            )
        }
    }
}
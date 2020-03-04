package com.tekzee.amiggos.ui.memories.mymemories.model


import com.google.gson.annotations.SerializedName

data class MyMemorieNewResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: Boolean = false
) {
    data class Data(
        @SerializedName("my_story_list")
        var myStoryList: List<MyStory> = listOf()
    ) {
        data class MyStory(
            @SerializedName("api_url")
            var apiUrl: String = "",
            @SerializedName("content")
            var content: List<Content> = listOf(),
            @SerializedName("imageUrl")
            var imageUrl: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("status")
            var status: Int = 0,
            @SerializedName("userid")
            var userid: Int = 0
        ) {
            data class Content(
                @SerializedName("api_url")
                var apiUrl: String = "",
                @SerializedName("created_at")
                var createdAt: String = "",
                @SerializedName("hours")
                var hours: Int = 0,
                @SerializedName("id")
                var id: Int = 0,
                @SerializedName("minutes")
                var minutes: Int = 0,
                @SerializedName("thumb_video")
                var thumbVideo: String = "",
                @SerializedName("time_ago")
                var timeAgo: String = "",
                @SerializedName("type")
                var type: String = "",
                @SerializedName("url")
                var url: String = "",
                @SerializedName("video_thumb")
                var videoThumb: String = "",
                @SerializedName("viewCount")
                var viewCount: Int = 0
            )
        }
    }
}
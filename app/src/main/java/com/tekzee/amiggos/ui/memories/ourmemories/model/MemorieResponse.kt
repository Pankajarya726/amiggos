package com.tekzee.amiggos.ui.memories.ourmemories.model
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MemorieResponse(
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
): Serializable {
    data class Data(
        @SerializedName("memories_list")
        val memoriesList: List<Memories> = listOf()
    ) {
        data class  Memories(
            @SerializedName("memory")
            val memory: List<Memory> = listOf(),
            @SerializedName("name")
            val name: String = "",
            @SerializedName("profile")
            val profile: String = "",
            @SerializedName("thumb_image")
            val thumb_image: String = "",
            @SerializedName("venue_id")
            val venueId: Int = 0,
            @SerializedName("type")
            val type: Int = 0,
            @SerializedName("our_story_id")
            val our_story_id: String = "0"
        ): Serializable {
            data class Memory(
                @SerializedName("creater_id")
                val creater_id : Int = 0,
                @SerializedName("viewCount")
                val viewCount : Int = 0,
                @SerializedName("file_type")
                val fileType: Int = 0,
                @SerializedName("id")
                val id: Int = 0,
                @SerializedName("profile")
                val profile: String = "",
                @SerializedName("user_id")
                val user_id: Int = 0,
                @SerializedName("story_file")
                val storyFile: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("thumb_video")
                val thumb_video: String = "",
                @SerializedName("video_thumb")
                val video_thumb: String = "",
                @SerializedName("tagged")
                val tagged: List<Tagged> = listOf(),
                @SerializedName("venue_id")
                val venueId: String = ""
            ): Serializable {

                data class Tagged(
                    @SerializedName("banner")
                    val banner: String = "",
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("website")
                    val website: String = ""
                ): Serializable

                override fun toString(): String {
                    return "Memory(creater_id=$creater_id, viewCount=$viewCount, fileType=$fileType, id=$id, storyFile='$storyFile', thumb_video='$thumb_video', video_thumb='$video_thumb', tagged=$tagged, venueId='$venueId')"
                }
            }
        }
    }
}
//package com.tekzee.amiggos.ui.storieviewnew.model
//import com.google.gson.annotations.SerializedName
//import java.io.Serializable
//
//data class MemorieResponse(
//    @SerializedName("data")
//    val `data`: Data = Data(),
//    @SerializedName("message")
//    val message: String = "",
//    @SerializedName("status")
//    val status: Boolean = false
//): Serializable {
//    data class Data(
//        @SerializedName("memories_list")
//        val memoriesList: List<Memories> = listOf()
//    ) {
//        data class Memories(
//            @SerializedName("memory")
//            val memory: List<Memory> = listOf(),
//            @SerializedName("name")
//            val name: String = "",
//            @SerializedName("profile")
//            val profile: String = "",
//            @SerializedName("venue_id")
//            val venueId: Int = 0,
//            @SerializedName("type")
//            val type: Int = 0
//        ): Serializable {
//            data class Memory(
//                @SerializedName("file_type")
//                val fileType: Int = 0,
//                @SerializedName("id")
//                val id: Int = 0,
//                @SerializedName("story_file")
//                val storyFile: String = "",
//                @SerializedName("tagged")
//                val tagged: List<Tagged> = listOf(),
//                @SerializedName("venue_id")
//                val venueId: String = ""
//            ): Serializable {
//                data class Tagged(
//                    @SerializedName("banner")
//                    val banner: String = "",
//                    @SerializedName("id")
//                    val id: Int = 0,
//                    @SerializedName("name")
//                    val name: String = "",
//                    @SerializedName("website")
//                    val website: String = ""
//                ): Serializable
//            }
//        }
//    }
//}
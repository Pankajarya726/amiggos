package com.tekzee.amiggos.ui.storieviewnew.data

import android.os.Parcelable
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val storyData: MemorieResponse.Data.Memories.Memory,
    val url: String,


//    val storyDate: Long,
    val banners: ArrayList<MemorieResponse.Data.Memories.Memory.Tagged>,
//    val creater_id: Int,
//    val viewCount: Int,
    val ourstorieid: String, val from: String
) : Parcelable {

    fun isVideo() =  url.contains(".mp4")

}
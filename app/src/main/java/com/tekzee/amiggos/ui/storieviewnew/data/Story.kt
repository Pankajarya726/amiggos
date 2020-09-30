package com.tekzee.amiggos.ui.storieviewnew.data

import android.os.Parcelable
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val storieId: String,
    val url: String,
    val storyDate: Long,
    val banners: ArrayList<MemorieResponse.Data.Memories.Memory.Tagged>
) : Parcelable {

    fun isVideo() =  url.contains(".mp4")
}
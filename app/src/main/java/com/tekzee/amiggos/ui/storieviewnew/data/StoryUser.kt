package com.tekzee.amiggos.ui.storieviewnew.data

import android.os.Parcelable
import com.tekzee.amiggos.ui.storieviewnew.data.Story
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryUser(
    val username: String, val profilePicUrl: String, val stories: ArrayList<Story>,
) : Parcelable
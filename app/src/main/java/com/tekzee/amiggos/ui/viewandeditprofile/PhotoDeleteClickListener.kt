package com.tekzee.amiggos.ui.viewandeditprofile

import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse

interface PhotoDeleteClickListener {
    fun OnPhotoDeleteClicked(position: Int, photoid: String)
    fun onUpdateProfile(listItem: GetUserProfileResponse.Data.OtherImage)
}
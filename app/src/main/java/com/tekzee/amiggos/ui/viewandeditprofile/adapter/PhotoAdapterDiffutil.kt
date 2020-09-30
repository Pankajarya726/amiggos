package com.tekzee.amiggos.ui.viewandeditprofile.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse


class PhotoAdapterDiffutil : DiffUtil.ItemCallback<GetUserProfileResponse.Data.OtherImage>() {
    override fun areItemsTheSame(oldItem: GetUserProfileResponse.Data.OtherImage, newItem: GetUserProfileResponse.Data.OtherImage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GetUserProfileResponse.Data.OtherImage, newItem: GetUserProfileResponse.Data.OtherImage): Boolean {
        return oldItem == newItem
    }


}
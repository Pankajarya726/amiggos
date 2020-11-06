package com.tekzee.amiggos.ui.addusers.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.addusers.model.AddUserResponse


class AddUserDiffutil : DiffUtil.ItemCallback<AddUserResponse.Data.Staff>() {
    override fun areItemsTheSame(oldItem: AddUserResponse.Data.Staff, newItem: AddUserResponse.Data.Staff): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AddUserResponse.Data.Staff, newItem: AddUserResponse.Data.Staff): Boolean {
        return oldItem == newItem
    }


}
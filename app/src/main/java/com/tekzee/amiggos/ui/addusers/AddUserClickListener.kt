package com.tekzee.amiggosvenueapp.ui.addusers

import com.tekzee.amiggosvenueapp.ui.addusers.model.AddUserResponse

interface AddUserClickListener {
    fun onItemClicked(position: Int, listItem: AddUserResponse.Data.Staff)
}
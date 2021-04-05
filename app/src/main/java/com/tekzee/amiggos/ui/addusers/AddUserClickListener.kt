package com.tekzee.amiggos.ui.addusers

import com.tekzee.amiggos.ui.addusers.model.AddUserResponse

interface AddUserClickListener {
    fun onItemClicked(position: Int, listItem: AddUserResponse.Data.Staff)
}
package com.tekzee.amiggos.ui.menu.commonfragment

import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse

interface CommonClickListener {
    fun onItemClicked(
        position: Int,
        listItem: Menu,
        quantity: String
    )


    fun onChatButtonClicked(
        position: Int,
        listItem: CommonMenuResponse.Data.Staff
    )


}
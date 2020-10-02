package com.tekzee.amiggos.ui.menu.commonfragment

import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse

interface CommonClickListener {
    fun onItemClicked(
        position: Int,
        status:Boolean,
        listItem: CommonMenuResponse.Data.Staff
    )


    fun onChatButtonClicked(
        position: Int,
        listItem: CommonMenuResponse.Data.Staff
    )
}
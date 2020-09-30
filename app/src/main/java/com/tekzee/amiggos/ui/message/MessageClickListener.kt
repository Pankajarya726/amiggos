package com.tekzee.amiggos.ui.message

import com.tekzee.amiggos.ui.message.model.MyConversation

interface MessageClickListener {
    fun onItemClicked(
        position: Int,
        listItem: MyConversation
    )
}
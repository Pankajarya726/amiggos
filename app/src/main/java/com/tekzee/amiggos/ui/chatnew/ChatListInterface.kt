package com.tekzee.amiggos.ui.chatnew

import com.tekzee.amiggos.ui.message.model.Message


interface ChatListInterface {
    fun getChatList(chatListArray: Message)
}
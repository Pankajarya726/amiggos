package com.tekzee.amiggos.ui.chat.interfaces

import com.tekzee.amiggos.ui.chat.model.Message

interface ChatListInterface {
    fun getChatList(chatListArray: Message)
}
package com.tekzee.amiggos.ui.chat.interfaces

import com.tekzee.amiggos.firebasemodel.User

interface ReceiverIdInterface {
    fun getReceiverId(receiverId: String, user: User)
}
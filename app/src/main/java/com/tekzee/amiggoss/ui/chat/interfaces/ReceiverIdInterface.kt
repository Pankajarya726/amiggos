package com.tekzee.amiggoss.ui.chat.interfaces

import com.tekzee.amiggoss.firebasemodel.User

interface ReceiverIdInterface {
    fun getReceiverId(receiverId: String, user: User)
}
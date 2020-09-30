package com.tekzee.amiggos.ui.chatnew

import com.tekzee.amiggos.firebasemodel.User


interface ReceiverIdInterface {
    fun getReceiverId(receiverId: String, user: User)
}
package com.tekzee.amiggos.ui.chat.myfriendchatlist

import com.tekzee.amiggos.ui.chat.myfriendchatlist.model.MyFriendChatModel

interface MyFriendChatListListener {
    fun onMyFriendChatListClicked(myFriendChatModel: MyFriendChatModel)
}
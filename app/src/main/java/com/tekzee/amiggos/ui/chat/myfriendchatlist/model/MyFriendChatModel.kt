package com.tekzee.amiggos.ui.chat.myfriendchatlist.model

data class MyFriendChatModel(
    var amiggosID: String? = "",
    var deviceToken: String? = "",
    var email: String? = "",
    var fcmToken: String? = "",
    var image: String? = "",
    var name: String? = "",
    var time: String? = "",
    var lastmessage: String? = "",
    var unreadmessage: String? = ""

)
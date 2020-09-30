package com.tekzee.amiggos.ui.viewfriends

import com.tekzee.amiggos.ui.viewfriends.model.StorieViewData

interface ViewFriendInterface {
    fun onFriendClicked(
        position: Int,
        storieViewData: StorieViewData
    )
}
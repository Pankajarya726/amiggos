package com.tekzee.amiggoss.ui.viewfriends

import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewData

interface ViewFriendInterface {
    fun onFriendClicked(
        position: Int,
        storieViewData: StorieViewData
    )
}
package com.tekzee.amiggoss.ui.blockedusers

import com.tekzee.amiggoss.ui.blockedusers.model.BlockedUserResponse

interface UnblockListener {
    fun onUnblockClicked(blockedUser: BlockedUserResponse.Data.BlockedUser)
    fun onItemClicked(blockedUser: BlockedUserResponse.Data.BlockedUser)
}
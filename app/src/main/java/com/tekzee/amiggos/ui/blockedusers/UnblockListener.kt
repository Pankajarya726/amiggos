package com.tekzee.amiggos.ui.blockedusers

import com.tekzee.amiggos.ui.blockedusers.model.BlockedUserResponse

interface UnblockListener {
    fun onUnblockClicked(blockedUser: BlockedUserResponse.Data.BlockedUser)
    fun onItemClicked(blockedUser: BlockedUserResponse.Data.BlockedUser)
}
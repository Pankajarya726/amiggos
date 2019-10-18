package com.tekzee.amiggos.ui.realfriends.invitations.interfaces

import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationData

interface InvitationInterfaces {
    fun onItemClicked(
        invitationData: InvitationData,
        type: Int
    )
}
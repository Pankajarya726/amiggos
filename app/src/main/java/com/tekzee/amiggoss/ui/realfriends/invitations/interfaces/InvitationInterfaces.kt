package com.tekzee.amiggoss.ui.realfriends.invitations.interfaces

import com.tekzee.amiggoss.ui.realfriends.invitations.model.InvitationResponseV2

interface InvitationInterfaces {
    fun onItemClicked(
        invitationData: InvitationResponseV2.Data.FreindRequest,
        type: Int
    )
}
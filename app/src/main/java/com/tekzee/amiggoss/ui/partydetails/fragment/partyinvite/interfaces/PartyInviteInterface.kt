package com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.interfaces

import com.tekzee.amiggoss.ui.partydetails.fragment.partyinvite.model.PartyInvitesData

interface PartyInviteInterface {
    fun onItemClicked(
        partyinvitesData: PartyInvitesData,
        i: Int
    )
}
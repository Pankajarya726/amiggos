package com.tekzee.amiggoss.ui.partydetails.fragment.pastparty.interfaces

import com.tekzee.amiggoss.ui.partydetails.fragment.pastparty.model.PastPartyData

interface PastPartyInterface {
    fun onItemClicked(
        partyinvitesData: PastPartyData,
        i: Int
    )
}
package com.tekzee.amiggos.ui.partydetails.fragment.pastparty.interfaces

import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyData

interface PastPartyInterface {
    fun onItemClicked(
        partyinvitesData: PastPartyData,
        i: Int
    )
}
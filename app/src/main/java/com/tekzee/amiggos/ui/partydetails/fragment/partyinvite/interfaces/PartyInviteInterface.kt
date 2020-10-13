package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.interfaces

import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse

interface PartyInviteInterface {
    fun onItemClicked(
        partyinvitesData: BookingInvitationResponse.Data.BookingDetail,
        i: Int
    )
}
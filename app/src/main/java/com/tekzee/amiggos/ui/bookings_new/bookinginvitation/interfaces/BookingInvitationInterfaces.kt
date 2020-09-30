package com.tekzee.amiggos.ui.bookings_new.bookinginvitation.interfaces

import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse

interface BookingInvitationInterfaces {
    fun onItemClicked(
        invitationData: BookingInvitationResponse.Data.BookingDetail,
        type: Int
    )
}
package com.tekzee.amiggos.ui.bookings_new.bookings

import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse

interface BookingClickedListener {

    fun onBookingClicked(upcomingParty: ABookingResponse.Data.BookingData)
}
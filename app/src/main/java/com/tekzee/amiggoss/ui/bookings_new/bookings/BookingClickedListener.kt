package com.tekzee.amiggoss.ui.bookings_new.bookings

import com.tekzee.amiggoss.ui.bookings_new.bookings.model.ABookingResponse

interface BookingClickedListener {

    fun onBookingClicked(upcomingParty: ABookingResponse.Data.BookingData)
}
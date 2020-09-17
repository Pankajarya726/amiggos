package com.tekzee.amiggoss.ui.mybooking

import com.tekzee.amiggoss.ui.mybooking.model.MyBookingData

interface BookingClicked {
    fun onBookingClicked(position: Int,selectedData: MyBookingData)
}
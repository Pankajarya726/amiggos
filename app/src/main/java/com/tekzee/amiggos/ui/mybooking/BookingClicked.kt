package com.tekzee.amiggos.ui.mybooking

import com.tekzee.amiggos.ui.mybooking.model.MyBookingData

interface BookingClicked {
    fun onBookingClicked(position: Int,selectedData: MyBookingData)
}
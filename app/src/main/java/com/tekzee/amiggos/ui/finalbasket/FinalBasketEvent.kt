package com.tekzee.amiggos.ui.finalbasket

import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse

interface FinalBasketEvent {

    fun onStarted()
    fun onCreateBookingSuccess(response: CreateBookingResponse)
    fun onCreateBookingFailure(message: String)
//    fun onLoaded()
//    fun onChangeStatusStarted()
    fun onFailure(message: String)
//    fun onStatusUpdated(message: String)
    fun sessionExpired(message: String)

}
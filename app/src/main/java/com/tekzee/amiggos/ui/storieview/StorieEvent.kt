package com.tekzee.amiggos.ui.storieview

interface StorieEvent {

    fun onAcceptDeclineCalled()
    fun onAcceptDeclineResponse(message: String)
    fun onFailure(message: String)
    fun sessionExpired(message: String)


}
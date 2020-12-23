package com.tekzee.amiggos.ui.storieview

interface StorieEvent {

    fun onAcceptDeclineCalled()
    fun onAcceptDeclineResponse(message: String)
    fun onDeleteResponse(message: String, counter: Int)
    fun onFailure(message: String)
    fun sessionExpired(message: String)


}
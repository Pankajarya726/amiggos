package com.tekzee.amiggosvenueapp.ui.tagging

interface TaggingEvent {

    fun onStarted()
    fun onLoaded()
    fun onFailure(message: String)
    fun sessionExpired(message: String)

}
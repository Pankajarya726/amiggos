package com.tekzee.amiggos.ui.addusers

import com.tekzee.amiggos.ui.addusers.model.AddUserResponse

interface AddUserEvent {

    fun onStarted()
    fun onLoaded(response: AddUserResponse)
    fun onFailure(message: String)
    fun sessionExpired(message: String)
    fun onBackButtonPressed()
}
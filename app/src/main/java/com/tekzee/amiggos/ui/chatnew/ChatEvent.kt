package com.tekzee.amiggos.ui.chatnew

interface ChatEvent {
    fun onBackButtonPressed()
    fun isBlockedUserSuccess()
    fun isBlockedUserFailure(message: String)
    fun onFailure(message: String)
    fun sessionExpired(message: String)

}
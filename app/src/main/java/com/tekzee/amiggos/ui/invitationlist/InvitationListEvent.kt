package com.tekzee.amiggos.ui.invitationlist

import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse

interface InvitationListEvent {

    fun onStarted()
    fun onLoaded(response: InvitationListResponse)
    fun onFailure(message: String)
    fun sessionExpired(message: String)
    fun onBackButtonPressed()
}
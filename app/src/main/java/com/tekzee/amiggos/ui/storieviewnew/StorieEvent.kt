package com.tekzee.amiggos.ui.storieview

import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse

interface StorieEvent {

    fun onAcceptDeclineCalled()
    fun onAcceptDeclineResponse(message: String)
    fun onDeleteResponse(message: String, counter: Int)
    fun onFailure(message: String)
    fun sessionExpired(message: String)
    fun onBannerCountSuccess(message: String, listItem: MemorieResponse.Data.Memories.Memory.Tagged)


}
package com.tekzee.amiggos.ui.menu

import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse
import com.tekzee.amiggos.ui.menu.model.MenuResponse

interface MenuEvent {
    fun onStarted()
    fun onLoaded(menuList: List<MenuResponse.Data.Section>, response: MenuResponse)
    fun onFailure(message: String)
    fun sessionExpired(message: String)
    fun onDrawerClicked()


    fun onCreateBookingSuccess(response: CreateBookingResponse)
    fun onCreateBookingFailure(message: String)
}
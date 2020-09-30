package com.tekzee.amiggosvenueapp.ui.chat

import com.tekzee.amiggos.ui.chatnew.model.ChatMessage

interface ChatActivityListener {

    fun onItemClicked(position: Int, listItem: ChatMessage)
}
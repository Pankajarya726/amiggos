package com.tekzee.amiggos.ui.chatnew.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.chatnew.model.ChatMessage

class ChatNewDiffutil : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem.timeSpam == newItem.timeSpam
    }

    override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
        return oldItem == newItem
    }


}
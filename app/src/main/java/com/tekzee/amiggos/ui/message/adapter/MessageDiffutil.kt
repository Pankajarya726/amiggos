package com.tekzee.amiggos.ui.message.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.message.model.MyConversation

class MessageDiffutil : DiffUtil.ItemCallback<MyConversation>() {
    override fun areItemsTheSame(oldItem: MyConversation, newItem: MyConversation): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: MyConversation, newItem: MyConversation): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }


}
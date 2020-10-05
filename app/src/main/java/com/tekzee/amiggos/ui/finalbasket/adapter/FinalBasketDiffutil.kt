package com.tekzee.amiggos.ui.finalbasket.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.room.entity.Menu

class FinalBasketDiffutil : DiffUtil.ItemCallback<Menu>() {
    override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
        return oldItem.id == newItem.id
    }


}
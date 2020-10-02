package com.tekzee.amiggos.ui.menu.commonfragment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse

class CommonDiffutil : DiffUtil.ItemCallback<CommonMenuResponse.Data.Staff>() {
    override fun areItemsTheSame(oldItem: CommonMenuResponse.Data.Staff, newItem: CommonMenuResponse.Data.Staff): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: CommonMenuResponse.Data.Staff, newItem: CommonMenuResponse.Data.Staff): Boolean {
        return oldItem == newItem
    }


}
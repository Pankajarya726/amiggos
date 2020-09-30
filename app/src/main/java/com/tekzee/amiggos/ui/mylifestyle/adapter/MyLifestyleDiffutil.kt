package com.tekzee.amiggos.ui.mylifestyle.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse


class MyLifestyleDiffutil : DiffUtil.ItemCallback<MyLifestyleResponse.Data.Lifestyle>() {
    override fun areItemsTheSame(oldItem: MyLifestyleResponse.Data.Lifestyle, newItem: MyLifestyleResponse.Data.Lifestyle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MyLifestyleResponse.Data.Lifestyle, newItem: MyLifestyleResponse.Data.Lifestyle): Boolean {
        return oldItem == newItem
    }


}
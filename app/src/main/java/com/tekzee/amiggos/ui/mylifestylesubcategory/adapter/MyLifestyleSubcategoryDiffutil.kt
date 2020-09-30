package com.tekzee.amiggos.ui.mylifestylesubcategory.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse


class MyLifestyleSubcategoryDiffutil : DiffUtil.ItemCallback<MyLifestyleSubcategoryResponse.Data.Lifestyle>() {
    override fun areItemsTheSame(oldItem: MyLifestyleSubcategoryResponse.Data.Lifestyle, newItem: MyLifestyleSubcategoryResponse.Data.Lifestyle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MyLifestyleSubcategoryResponse.Data.Lifestyle, newItem: MyLifestyleSubcategoryResponse.Data.Lifestyle): Boolean {
        return oldItem == newItem
    }


}
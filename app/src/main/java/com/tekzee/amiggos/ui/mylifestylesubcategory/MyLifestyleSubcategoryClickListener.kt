package com.tekzee.amiggos.ui.mylifestylesubcategory

import com.tekzee.amiggos.ui.mylifestylesubcategory.model.MyLifestyleSubcategoryResponse

interface MyLifestyleSubcategoryClickListener {
    fun onItemClick(position: MyLifestyleSubcategoryResponse.Data.Lifestyle, position1: Int)

}
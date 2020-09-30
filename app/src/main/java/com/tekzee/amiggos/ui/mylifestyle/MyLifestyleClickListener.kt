package com.tekzee.amiggos.ui.mylifestyle

import com.tekzee.amiggos.ui.mylifestyle.model.MyLifestyleResponse

interface MyLifestyleClickListener {
    fun onItemClick(position: MyLifestyleResponse.Data.Lifestyle)

}
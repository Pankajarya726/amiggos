package com.tekzee.amiggos.ui.finalbasket

import android.view.View
import android.widget.TextView
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse

interface FinalBasketClickListener {
    fun onItemClicked(
        position: Int,
        listItem: Menu,
        quantity: String
    )


    fun onChatButtonClicked(
        position: Int,
        listItem: CommonMenuResponse.Data.Staff
    )

    fun showAgeRestrictionPopup(view: View)
    fun viewImage(listItem: Menu?)

}
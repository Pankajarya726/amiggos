package com.tekzee.amiggos.ui.menu.commonfragment

import android.view.View
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse

interface CommonClickListener {
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
    fun showNotVerifiedMessage(isIdproofNotverifiedMessage: String)
    fun showAlertForAgeRestriction(isIdproofNotverifiedMessage: String)
    fun viewImage(listItem: Menu?)

}
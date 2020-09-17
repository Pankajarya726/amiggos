package com.tekzee.amiggoss.ui.mypreferences.interfaces

import com.tekzee.amiggoss.ui.mypreferences.model.MyPreferenceData

interface MyPreferenceClicked {
    fun onMyPreferenceClicked(
        position: Int,
        selectedData: MyPreferenceData
    )

    fun lastPosition(
        position: Int

    )
}
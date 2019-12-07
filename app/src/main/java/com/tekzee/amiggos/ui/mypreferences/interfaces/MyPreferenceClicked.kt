package com.tekzee.amiggos.ui.mypreferences.interfaces

import com.tekzee.amiggos.ui.mypreferences.model.MyPreferenceData

interface MyPreferenceClicked {
    fun onMyPreferenceClicked(
        position: Int,
        selectedData: MyPreferenceData
    )

    fun lastPosition(
        position: Int

    )
}
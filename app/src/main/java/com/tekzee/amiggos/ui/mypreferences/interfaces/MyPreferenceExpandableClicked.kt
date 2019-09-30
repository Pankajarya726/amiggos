package com.tekzee.amiggos.ui.mypreferences.interfaces

import com.tekzee.amiggos.ui.mypreferences.model.Value

interface MyPreferenceExpandableClicked {
    fun onMyPreferenceExpandableClicked(
        position: Int,
        selectedData: Value
    )
}
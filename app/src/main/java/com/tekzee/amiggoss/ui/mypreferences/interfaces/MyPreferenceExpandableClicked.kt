package com.tekzee.amiggoss.ui.mypreferences.interfaces

import com.tekzee.amiggoss.ui.mypreferences.model.Value

interface MyPreferenceExpandableClicked {
    fun onMyPreferenceExpandableClicked(
        position: Int,
        selectedData: Value
    )
}
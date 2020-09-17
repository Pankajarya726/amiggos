package com.tekzee.amiggoss.ui.turningup.interfaces

import com.tekzee.amiggoss.ui.turningup.model.TurningUpData

interface TurningUpClicked {
    fun onTuringUpClicked(
        position: Int,
        selectedData: TurningUpData
    )
}
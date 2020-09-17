package com.tekzee.amiggoss.ui.settings.interfaces

import com.tekzee.amiggoss.ui.settings.model.SettingsData

interface SettingsInterface {
    fun onItemClicked(
        settingsData: SettingsData,
        b: Boolean
    )

    fun onTextClicked(settingsData: SettingsData)
}
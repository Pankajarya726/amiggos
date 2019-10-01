package com.tekzee.amiggos.ui.settings.interfaces

import com.tekzee.amiggos.ui.settings.model.SettingsData

interface SettingsInterface {
    fun onItemClicked(
        settingsData: SettingsData,
        b: Boolean
    )

    fun onTextClicked(settingsData: SettingsData)
}
package com.tekzee.amiggos.ui.settings.interfaces

import com.tekzee.amiggos.ui.settings.model.SettingsResponse

interface SettingsInterface {
    fun onItemClicked(
        settingsData: SettingsResponse.Data.Setting,
        b: Boolean
    )

    fun onTextClicked(settingsData: SettingsResponse.Data.Setting)
}
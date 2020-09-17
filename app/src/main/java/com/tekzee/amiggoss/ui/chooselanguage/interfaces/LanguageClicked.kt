package com.tekzee.amiggoss.ui.chooselanguage.interfaces

import com.tekzee.amiggoss.ui.chooselanguage.model.Language

interface LanguageClicked {
    fun onLanguageClicked(
        position: Int,
        switchClicked: Boolean,
        selectedData: Language
    )
}
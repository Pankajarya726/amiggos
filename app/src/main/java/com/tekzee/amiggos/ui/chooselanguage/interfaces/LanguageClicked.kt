package com.tekzee.amiggos.ui.chooselanguage.interfaces

import com.tekzee.amiggos.ui.chooselanguage.model.Language

interface LanguageClicked {
    fun onLanguageClicked(position: Int,selectedData: Language)
}
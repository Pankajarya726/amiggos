package com.tekzee.amiggoss.ui.chooselanguage.model

data class Language(
    val id: Int,
    val image: String,
    val iso2_code: String,
    val name: String,
    var isChecked: Boolean = false

)
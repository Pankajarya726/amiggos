package com.tekzee.amiggos.ui.mainsplash.model

data class ValidateAppVersionResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean,
    val update_type: Int
)
package com.tekzee.amiggos.ui.login.model

data class LoginResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)
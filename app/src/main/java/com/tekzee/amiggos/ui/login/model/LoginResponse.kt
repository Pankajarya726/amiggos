package com.tekzee.amiggos.ui.login.model

data class LoginResponse(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val status: Boolean = false
)
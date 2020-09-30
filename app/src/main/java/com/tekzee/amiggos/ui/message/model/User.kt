package com.tekzee.amiggos.ui.message.model
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    var amiggosID: String? = "",
    var deviceToken: String? = "",
    var email: String? = "",
    var fcmToken: String? = "",
    var image: String? = "",
    var name: String? = "",
    var timestamp: Long? = 0
):Serializable
package com.tekzee.amiggos.firebasemodel

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var amiggosID: String? = "",
    var deviceToken: String? = "",
    var email: String? = "",
    var fcmToken: String? = "",
    var image: String? = "",
    var name: String? = "",
    var timestamp: Long? = 0
) {
    override fun toString(): String {
        return "User(amiggosID=$amiggosID, deviceToken=$deviceToken, email=$email, fcmToken=$fcmToken, image=$image, name=$name, timestamp=$timestamp)"
    }
}
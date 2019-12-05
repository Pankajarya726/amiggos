package com.tekzee.amiggos.ui.choosepackage.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("package_data")
    val packageData: List<PackageData> = listOf(),
    @SerializedName("user_first_package")
    val userFirstPackage: String = "" ,
    @SerializedName("user_document_status")
    val userDocumentStatus: String = ""
)
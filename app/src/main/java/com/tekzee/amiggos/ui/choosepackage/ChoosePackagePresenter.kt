package com.tekzee.amiggos.ui.choosepackage

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.choosepackage.model.PackageBookResponse
import com.tekzee.amiggos.ui.choosepackage.model.PackageResponse
import com.tekzee.amiggos.base.BaseMainView

class ChoosePackagePresenter {
    interface ChoosePackageMainPresenter{

        fun doCallPackageApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun doBookPackage(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }


    interface ChoosePackageMainView: BaseMainView {
        fun onChoosePackageSuccess(responseData: PackageResponse?)
        fun onBookPackageSuccess(responseData: PackageBookResponse?)
    }

}
package com.tekzee.amiggos.ui.myprofile

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.myprofile.model.MyProfileResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MyProfilePresenter {
    interface MyProfileMainPresenter{
        fun onStop()
        fun doMyProfile(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyProfileMainView: BaseMainView{
        fun onMyProfileSuccess(responseData: MyProfileResponse?)
    }
}
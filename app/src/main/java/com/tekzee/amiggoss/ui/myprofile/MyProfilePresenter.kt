package com.tekzee.amiggoss.ui.myprofile

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggoss.ui.myprofile.model.MyProfileResponse
import com.tekzee.mallortaxi.base.BaseMainView

class MyProfilePresenter {
    interface MyProfileMainPresenter{
        fun onStop()
        fun doMyProfile(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doGetMyStoryByUserId(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface MyProfileMainView: BaseMainView{
        fun onMyProfileSuccess(responseData: MyProfileResponse?)
        fun onMyStoryByUserIdSuccess(responseData: OurMemoriesResponse?)
    }
}
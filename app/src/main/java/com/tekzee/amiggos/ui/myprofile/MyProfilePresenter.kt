package com.tekzee.amiggos.ui.myprofile

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggos.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggos.base.BaseMainView

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

    interface MyProfileMainView: BaseMainView {
        fun onMyProfileSuccess(responseData: MyProfileResponse?)
        fun onMyStoryByUserIdSuccess(responseData: OurMemoriesResponse?)
    }
}
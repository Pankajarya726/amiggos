package com.tekzee.amiggos.ui.agegroup

import com.google.gson.JsonObject
import com.tekzee.mallortaxi.base.BaseMainView
import com.tekzee.mallortaxi.base.model.CommonResponse

class AgeGroupActivityPresenter {
    interface AgeGroupPresenterMain{
        fun doCallAgeGroupApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()

    }

    interface AgeGroupMainView: BaseMainView{
        fun onAgeGroupApiSuccess(responseData: CommonResponse)
    }
}
package com.tekzee.amiggoss.ui.agegroup

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.agegroup.model.AgeGroupResponse
import com.tekzee.mallortaxi.base.BaseMainView

class AgeGroupActivityPresenter {
    interface AgeGroupPresenterMain{
        fun doCallAgeGroupApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()

    }

    interface AgeGroupMainView: BaseMainView{
        fun onAgeGroupApiSuccess(responseData: AgeGroupResponse)
    }
}
package com.tekzee.amiggoss.ui.chooseweek

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ChooseWeekPresenter {
    interface ChooseWeekMainPresenter{

        fun doCallWeekApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }


    interface ChooseWeekMainView: BaseMainView{
        fun onChooseWeekSuccess(responseData: ChooseWeekResponse?)
    }

}
package com.tekzee.amiggoss.ui.venuedetails

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.mallortaxi.base.BaseMainView

class VenueDetailsPresenter {

    interface VenueDetailsMainPresenter{
        fun onStop()

        fun doCallWeekApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface VenueDetailsMainView: BaseMainView{
        fun onChooseWeekSuccess(responseData: ChooseWeekResponse?)
    }
}
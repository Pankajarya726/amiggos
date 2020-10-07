package com.tekzee.amiggos.ui.calendarview

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.calendarview.model.TimeSlotResponse

class CalendarViewPresenter {
    interface CalendarViewMainPresenter{
        fun onStop()
        fun callGetTimeSlot(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface CalendarMainView: BaseMainView {

        fun onTimeSlotSuccess(responseData: TimeSlotResponse?)
        fun onTimeSlotFailure(responseData: String)

    }
}
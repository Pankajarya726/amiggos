package com.tekzee.amiggos.ui.bookings_new.bookings

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggos.base.BaseMainView

class ABookingPresenter{

    interface ABookingPresenterMain{
        fun onStop()

        fun docallGetBookings(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface ABookingPresenterMainView: BaseMainView {
        fun showProgress()
        fun hideProgress()
        fun onBookingSuccess(
            taggedVenue: List<ABookingResponse.Data.BookingData>,
            responseData: ABookingResponse
        )
        fun onBookingFailure(message: String)
    }
}
package com.tekzee.amiggoss.ui.bookings_new.bookings

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.mallortaxi.base.BaseMainView

class ABookingPresenter{

    interface ABookingPresenterMain{
        fun onStop()
        fun docallGetBookings(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface ABookingPresenterMainView: BaseMainView {
        fun onBookingSuccess(taggedVenue: List<ABookingResponse.Data.BookingData>)
        fun onBookingFailure(message: String)
    }
}
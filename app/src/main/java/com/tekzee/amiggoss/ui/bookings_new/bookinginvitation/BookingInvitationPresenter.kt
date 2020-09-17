package com.tekzee.amiggoss.ui.bookings_new.bookinginvitation

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import com.tekzee.mallortaxi.base.BaseMainView

class BookingInvitationPresenter {

    interface BookingInvitationMainPresenter{
        fun onStop()
        fun doCallBookingInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            fragmentVisible: Boolean
        )

        fun doAcceptBookingInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doRejectBookingInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

    }

    interface BookingInvitationMainView : BaseMainView{
        fun onInvitaionSuccess(responseData: BookingInvitationResponse?)
        fun onInvitationFailure(responseData: String)
        fun onAcceptInvitation(responseData: CommonResponse?)
        fun onRejectInvitation(responseData: CommonResponse?)

    }
}
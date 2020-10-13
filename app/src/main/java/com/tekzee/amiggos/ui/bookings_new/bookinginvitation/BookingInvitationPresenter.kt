package com.tekzee.amiggos.ui.bookings_new.bookinginvitation

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse

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

    interface BookingInvitationMainView : BaseMainView {
        fun onInvitaionSuccess(responseData: BookingInvitationResponse?)
        fun onInvitationFailure(responseData: String)
        fun onAcceptInvitation(responseData: CommonResponse?)
        fun onRejectInvitation(responseData: CommonResponse?)

    }
}
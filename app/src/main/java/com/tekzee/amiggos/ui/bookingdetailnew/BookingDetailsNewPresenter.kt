package com.tekzee.amiggos.ui.bookingdetailnew

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.base.BaseMainView
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse

class BookingDetailsNewPresenter {
    interface ABookingDetailsPresenterMain{
        fun dogetBookingDetails(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun onStop()
    }

    interface ABookingDetailsPresenterMainView: BaseMainView {
        fun onFriendListSuccess(responseData: FriendListResponse?)
        fun onBookingDetailSuccess(responseData: BookingDetailsNewResponse?)
        fun onBookingDetailFailure(responseData: String)
        fun onFriendInviteSuccess(responseData: CommonResponse?)
    }
}
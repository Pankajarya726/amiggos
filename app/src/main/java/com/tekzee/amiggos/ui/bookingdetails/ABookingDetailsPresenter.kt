package com.tekzee.amiggos.ui.bookingdetails

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.base.BaseMainView

class ABookingDetailsPresenter {
    interface ABookingDetailsPresenterMain{
        fun doGetFriendList(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun doInviteFriend(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun onStop()
    }

    interface ABookingDetailsPresenterMainView: BaseMainView {
        fun onFriendListSuccess(responseData: FriendListResponse?)
        fun onFriendInviteSuccess(responseData: CommonResponse?)
    }
}
package com.tekzee.amiggoss.ui.bookingdetails

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.ui.friendlist.model.FriendListResponse
import com.tekzee.mallortaxi.base.BaseMainView

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

    interface ABookingDetailsPresenterMainView: BaseMainView{
        fun onFriendListSuccess(responseData: FriendListResponse?)
        fun onFriendInviteSuccess(responseData: CommonResponse?)
    }
}
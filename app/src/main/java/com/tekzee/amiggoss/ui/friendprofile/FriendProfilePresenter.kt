package com.tekzee.amiggoss.ui.friendprofile

import com.google.gson.JsonObject
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggoss.ui.notification.model.StorieResponse
import com.tekzee.amiggoss.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.mallortaxi.base.BaseMainView

class FriendProfilePresenter {

    interface FriendProfileMainPresenter{
        fun onStop()
        fun doCallGetFriendProfileApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doAcceptInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doRejectInvitationApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doSendFriendRequest(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun callunFriend(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun callUnBlock(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )


        fun callReport(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun callBlock(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallStorieViewApi(
            notificationId: String,
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
    }

    interface FriendProfileMainView: BaseMainView{

        fun onFriendProfileSuccess(responseData: FriendProfileResponse?)
        fun onFriendProfileV2Success(responseData: GetFriendProfileDetailsResponse?)
        fun onAcceptInvitation(responseData: CommonResponse?)
        fun onRejectInvitation(responseData: CommonResponse?)
        fun onSendFriendRequestSuccess(responseData: CommonResponse?)
        fun onUnFriendSuccess(responseData: CommonResponse?)
        fun onUnBlockSuccess(responseData: CommonResponse?)
        fun onCallReportSuccess(responseData: CommonResponse?)
        fun onBlockSuccess(responseData: CommonResponse?)
        fun onStorieSuccess(
            responseData: StorieResponse,
            notificationId: String
        )

    }
}
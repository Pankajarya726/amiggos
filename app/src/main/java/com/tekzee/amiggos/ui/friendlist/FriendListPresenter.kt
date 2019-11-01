package com.tekzee.amiggos.ui.friendlist

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.mallortaxi.base.BaseMainView

class FriendListPresenter {
    interface FriendListMainPresenter{
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

    interface FriendListMainView: BaseMainView{
        fun onFriendListSuccess(responseData: FriendListResponse?)
        fun onFriendInviteSuccess(responseData: CommonResponse?)
    }
}
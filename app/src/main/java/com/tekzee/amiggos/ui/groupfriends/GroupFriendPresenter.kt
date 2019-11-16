package com.tekzee.amiggos.ui.groupfriends

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class GroupFriendPresenter {

    interface GroupFriendPresenterMain{
        fun onStop()
        fun getNearByUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface GroupFriendMainView: BaseMainView{
        fun onOnlineFriendSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendInfiniteSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendFailure(responseData: String)
    }
}
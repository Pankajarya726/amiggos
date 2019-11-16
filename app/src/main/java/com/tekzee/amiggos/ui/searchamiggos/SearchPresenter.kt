package com.tekzee.amiggos.ui.searchamiggos

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class SearchPresenter {

    interface SearchPresenterMain{
        fun onStop()
        fun getNearByUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface SearchMainView: BaseMainView{
        fun onOnlineFriendSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendInfiniteSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendFailure(responseData: String)
    }
}
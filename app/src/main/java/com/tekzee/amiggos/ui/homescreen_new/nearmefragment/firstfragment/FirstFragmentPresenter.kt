package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.base.BaseMainView

class FirstFragmentPresenter{

    interface FirstFragmentPresenterMain{
        fun onStop()
        fun getNearByUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface FirstFragmentPresenterMainView: BaseMainView {
        fun onOnlineFriendSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendInfiniteSuccess(responseData: SearchFriendResponse?)
        fun onOnlineFriendFailure(responseData: String)
    }
}
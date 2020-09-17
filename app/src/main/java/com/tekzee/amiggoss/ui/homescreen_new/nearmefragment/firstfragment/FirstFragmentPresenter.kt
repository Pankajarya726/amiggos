package com.tekzee.amiggoss.ui.homescreen_new.nearmefragment.firstfragment

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.mallortaxi.base.BaseMainView

class FirstFragmentPresenter{

    interface FirstFragmentPresenterMain{
        fun onStop()
        fun getNearByUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean,
            fragmentVisible: Boolean
        )
    }

    interface FirstFragmentPresenterMainView: BaseMainView {
        fun onOnlineFriendSuccess(responseData: List<NearByV2Response.Data.NearestFreind>)
        fun onOnlineFriendInfiniteSuccess(responseData: List<NearByV2Response.Data.NearestFreind>)
        fun onOnlineFriendFailure(responseData: String)
    }
}
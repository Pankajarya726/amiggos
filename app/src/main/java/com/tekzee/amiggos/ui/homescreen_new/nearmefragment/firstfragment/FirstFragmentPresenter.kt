package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.amiggos.base.BaseMainView

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
        fun onOnlineFriendSuccess(
            responseData: List<NearByV2Response.Data.NearestFreind>,
            totalCount: Int,
            responseData1: NearByV2Response
        )
        fun onOnlineFriendInfiniteSuccess(
            responseData: List<NearByV2Response.Data.NearestFreind>,
            responseData1: NearByV2Response
        )
        fun onOnlineFriendFailure(responseData: String)
    }
}
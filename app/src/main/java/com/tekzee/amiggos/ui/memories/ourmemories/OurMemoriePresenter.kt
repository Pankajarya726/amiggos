package com.tekzee.amiggos.ui.memories.ourmemories

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.model.NearByV2Response
import com.tekzee.mallortaxi.base.BaseMainView

class OurMemoriePresenter{

    interface OurMemoriePresenterrMain{
        fun onStop()
        fun getNearByUser(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )

        fun doGetMyStories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )
    }

    interface OurMemoriePresenterMainView: BaseMainView {
        fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse)
        fun onMyStoriesSuccess(responseData: GetMyStoriesResponse)
        fun onMyStoriesFailure(message: String)

        fun onOnlineFriendSuccess(responseData: List<NearByV2Response.Data.NearestFreind>)
        fun onOnlineFriendInfiniteSuccess(responseData: List<NearByV2Response.Data.NearestFreind>)
        fun onOnlineFriendFailure(responseData: String)
    }
}
package com.tekzee.amiggos.ui.home

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.NearbyMeCountResponse
import com.tekzee.mallortaxi.base.BaseMainView

class HomeActivityPresenter {
    interface HomeActivityPresenterMain {

        fun doGetMyStories(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            requestDatFromServer: Boolean
        )

        fun doGetDashboardMapApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun getNearByUserCount(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doGetVenueApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>,
            isfirsttime: Boolean
        )


        fun onStop()

    }

    interface HomeActivityMainView : BaseMainView {

        fun onMyStoriesSuccess(responseData: GetMyStoriesResponse)
        fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse)
        fun onVenueResponseInfiniteSuccess(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?)
        fun onDashboardMapResponse(responseData: DashboardReponse?)
        fun onVenueResponse(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?)
        fun onMyStoriesFailure(message: String)
        fun onVenueFailure(message: String)
        fun onDashboardMapFailure(message: String)
        fun onNearByCountSuccess(responseData: NearbyMeCountResponse?)


    }
}
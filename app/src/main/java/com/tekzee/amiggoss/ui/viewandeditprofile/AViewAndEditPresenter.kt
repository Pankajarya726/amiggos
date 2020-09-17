package com.tekzee.amiggoss.ui.viewandeditprofile

import com.google.gson.JsonObject
import com.tekzee.amiggoss.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.mallortaxi.base.BaseMainView
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AViewAndEditPresenter  {

    interface AViewAndEditPresenterrMain{
        fun callGetProfile(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun doCallStateApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallCityApi(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )

        fun doCallUpdateProfileApi(
            file: MultipartBody.Part?,
            valueInt: RequestBody,
            date: RequestBody,
            createHeaders: RequestBody,
            cityIdRequestBody: RequestBody,
            stateIdRequestBody: RequestBody,
            phonenumberRequestBody: RequestBody,
            useridRequestBody: RequestBody,
            createHeaders1: HashMap<String, String?>
        )
        fun onStop()
    }

    interface AViewAndEditPresenterMainView: BaseMainView{
        fun onGetProfileSuccess(responseData: GetUserProfileResponse?)
        fun onStateSuccess(responseData: List<StateResponse.Data.States>)
        fun onCitySuccess(responseData: List<CityResponse.Data.City>)
        fun onProfileUpdateSuccess(message: UpdateProfileResponse?)
        fun onProfileUpdateFailure(message: String)
        fun onStateFailure(responseData: String)
        fun onCityFailure(responseData: String)
    }
}
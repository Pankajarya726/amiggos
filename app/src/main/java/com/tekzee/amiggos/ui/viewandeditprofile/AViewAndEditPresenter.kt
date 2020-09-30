package com.tekzee.amiggos.ui.viewandeditprofile

import com.google.gson.JsonObject
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggos.base.BaseMainView
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AViewAndEditPresenter  {

    interface AViewAndEditPresenterrMain{
        fun callGetProfile(
            input: JsonObject,
            createHeaders: HashMap<String, String?>
        )
        fun deletePhoto(
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

        fun updateProfileImage(
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

        fun doUpdateUserImageApi(
            file: MultipartBody.Part?,
            useridRequestBody: RequestBody,
            createHeaders1: HashMap<String, String?>
        )


        fun onStop()
    }

    interface AViewAndEditPresenterMainView: BaseMainView {
        fun onGetProfileSuccess(responseData: GetUserProfileResponse?)
        fun onStateSuccess(responseData: List<StateResponse.Data.States>)
        fun onCitySuccess(responseData: List<CityResponse.Data.City>)
        fun onProfileUpdateSuccess(message: UpdateProfileResponse?)
        fun onUploadImageSuccess(message: String)
        fun onUpdateProfileImage(message: String)
        fun onProfileUpdateFailure(message: String)
        fun onStateFailure(responseData: String)
        fun onCityFailure(responseData: String)
        fun onPhotoDeleted(message: String)
    }
}
package com.tekzee.amiggos.ui.viewandeditprofile

import android.content.Context
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.AddImageResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.UpdateProfileResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AViewAndEditImplementation(
    private var mainView: AViewAndEditPresenter.AViewAndEditPresenterMainView,
    context: Context
) : AViewAndEditPresenter.AViewAndEditPresenterrMain {


    var context: Context? = context
    private var disposable: Disposable? = null


    override fun callGetProfile(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.callGetProfile(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: GetUserProfileResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onGetProfileSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun deletePhoto(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.deletePhotoApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: AddImageResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onPhotoDeleted(responseData.message,responseData)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun updateProfileImage(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.updateProfileImage(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUpdateProfileImage(responseData.message)
                            } else {
                                mainView.validateError(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }




    override fun doCallStateApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallStateApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: StateResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onStateSuccess(responseData.data.statesList)
                            } else {
                                mainView.onStateFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onStateFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun doCallCityApi(input: JsonObject, createHeaders: HashMap<String, String?>) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallCityApi(input, createHeaders)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CityResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onCitySuccess(responseData.data.cityList)
                            } else {
                                mainView.onCityFailure(responseData.message)
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.onCityFailure(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun doCallUpdateProfileApi(
        fileMultipartBody: MultipartBody.Part?,
        firstnameRequestBody: RequestBody,
        lastnameRequestBody: RequestBody,
        dobRequestBody: RequestBody,
        cityIdRequestBody: RequestBody,
        stateIdRequestBody: RequestBody,
        phonenumberRequestBody: RequestBody,
        useridRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doCallUpdateProfileApi(
                fileMultipartBody,
                firstnameRequestBody,
                lastnameRequestBody,
                dobRequestBody,
                cityIdRequestBody,
                stateIdRequestBody,
                phonenumberRequestBody,
                useridRequestBody,
                createHeaders
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: UpdateProfileResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onProfileUpdateSuccess(responseData)
                            } else {
                                mainView.validateError(responseData.message.toString())
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun doUploadSingleProfileApi(
        fileMultipartBody: MultipartBody.Part?,
        useridRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doUploadeProfileApi(
                fileMultipartBody,
                useridRequestBody,
                createHeaders
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onSingleProfileUploadSuccess(responseData.message)
                            } else {
                                mainView.validateError(responseData.message.toString())
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }

    override fun doUpdateUserImageApi(
        file: Array<MultipartBody.Part?>,
        useridRequestBody: RequestBody,
        createHeaders1: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doUpdateImage(
                file,
                useridRequestBody,
                createHeaders1
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: AddImageResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUploadImageSuccess(responseData.message,responseData)
                            } else {
                                mainView.validateError(responseData.message.toString())
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun doUpdateUserSingleImageApi(
        file: MultipartBody.Part?,
        useridRequestBody: RequestBody,
        createHeaders1: HashMap<String, String?>
    ) {
        mainView.showProgressbar()
        if (mainView.checkInternet()) {
            disposable = ApiClient.instance.doUpdateSingleImage(
                file,
                useridRequestBody,
                createHeaders1
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    mainView.hideProgressbar()
                    val responseCode = response.code()
                    when (responseCode) {
                        200 -> {
                            val responseData: CommonResponse? = response.body()
                            if (responseData!!.status) {
                                mainView.onUploadImageSingleSuccess(responseData.message)
                            } else {
                                mainView.validateError(responseData.message.toString())
                            }
                        }
                    }
                }, { error ->
                    mainView.hideProgressbar()
                    mainView.validateError(error.message.toString())
                })
        } else {
            mainView.hideProgressbar()
            mainView.validateError(context!!.getString(R.string.check_internet))
        }
    }


    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}
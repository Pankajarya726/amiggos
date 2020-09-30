package com.tekzee.amiggos.base.repository

import com.tekzee.amiggos.network.ApiService
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody


class MemorieRepository(private val apiService: ApiService) {

    fun CreateMemorieApi(
        input: MultipartBody.Part,
        useridRequestBody: RequestBody,
        typeRequestBody: RequestBody,
        venueidRequestBody: RequestBody,
        taggedRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Single<ResponseBody> {
        return apiService.doCreateMemorieApi(input,useridRequestBody,typeRequestBody,venueidRequestBody,taggedRequestBody,createHeaders)
    }

    fun CreateOurMemorieApi(
        input: MultipartBody.Part,
        useridRequestBody: RequestBody,
        selectedFriends: RequestBody,
        ourstoridid: RequestBody,
        taggedRequestBody: RequestBody,
        createHeaders: HashMap<String, String?>
    ): Single<ResponseBody> {
        return apiService.doCreateOurMemorieApi(input,useridRequestBody,selectedFriends,ourstoridid,taggedRequestBody,createHeaders)
    }

}
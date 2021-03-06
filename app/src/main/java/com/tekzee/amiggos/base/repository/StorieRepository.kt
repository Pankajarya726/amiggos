package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import io.reactivex.Observable
import retrofit2.Response


class StorieRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun rejectOurStory(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.rejectOurStory(input,createHeaders) }
    }

    suspend fun doBannerCountApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.doBannerCountApi(input,createHeaders) }
    }

    suspend fun docallDeleteApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.docallDeleteApi(input,createHeaders) }
    }


    suspend fun doRejectCreateMemoryInvitationApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.doRejectCreateMemoryInvitationApiNew(input, createHeaders) }
    }
}
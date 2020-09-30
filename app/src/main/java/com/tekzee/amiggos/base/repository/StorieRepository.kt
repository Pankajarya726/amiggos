package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest


class StorieRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun docallAcceptDeclineApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.docallAcceptDeclineApi(input,createHeaders) }
    }

    suspend fun docallDeleteApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.docallDeleteApi(input,createHeaders) }
    }



}
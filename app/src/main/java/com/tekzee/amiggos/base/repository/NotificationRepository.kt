package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest


class NotificationRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun sendNotification(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.sendNotification(input,createHeaders) }
    }

    suspend fun checkUserisBlocked(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.checkUserisBlocked(input,createHeaders) }
    }
}
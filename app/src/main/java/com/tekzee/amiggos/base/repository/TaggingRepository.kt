package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse


class TaggingRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun doTaggingApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): TaggingResponse {
        return apiRequest { apiService.doGetTaggingApi(input,createHeaders) }
    }

}
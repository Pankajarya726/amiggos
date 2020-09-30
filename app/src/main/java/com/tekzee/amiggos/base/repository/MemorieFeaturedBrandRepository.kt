package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse


class MemorieFeaturedBrandRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun doGetFeaturedBrands(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): MemorieResponse {
        return apiRequest { apiService.newdoGetFeaturedBrands(input,createHeaders) }
    }

    suspend fun doGetMemorie(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): MemorieResponse {
        return apiRequest { apiService.doGetMemorie(input,createHeaders) }
    }

}
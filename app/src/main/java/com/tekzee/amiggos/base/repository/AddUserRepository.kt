package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.addusers.model.AddUserResponse


class AddUserRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun doAddUserApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): AddUserResponse {
        return apiRequest { apiService.doAddUserApi(input,createHeaders) }
    }

}
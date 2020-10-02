package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse


class StaffRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun doStaffApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): MenuResponse {
        return apiRequest { apiService.doStaffApi(input,createHeaders) }
    }

    suspend fun doStaffByIdApi(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonMenuResponse {
        return apiRequest { apiService.doStaffByIdApi(input,createHeaders) }
    }

    suspend fun doChangeUserStaus(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CommonResponse {
        return apiRequest { apiService.doChangeUserStaus(input,createHeaders) }
    }

}
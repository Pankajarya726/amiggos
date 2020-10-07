package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse


class FinalBasketRepository(private val apiService: ApiService): SafeApiRequest() {



    suspend fun callCreateBooking(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): CreateBookingResponse {
        return apiRequest { apiService.createBooking(input,createHeaders) }
    }

}
package com.tekzee.amiggos.base.repository

import com.google.gson.JsonObject
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.network.SafeApiRequest
import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse


class InvitationListRepository(private val apiService: ApiService): SafeApiRequest() {

    suspend fun doCallInvitationList(
        input: JsonObject,
        createHeaders: HashMap<String, String?>
    ): InvitationListResponse {
        return apiRequest { apiService.doCallInvitationList(input,createHeaders) }
    }

}
package com.tekzee.amiggos.network

import com.tekzee.amiggos.util.ApiException
import com.tekzee.amiggos.util.LogoutException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.StringBuilder

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()

        if (response.code() == 200 && response.isSuccessful) {
            return response.body()!!
        } else if (response.code() == 404) {
            throw LogoutException("Your session is expired ,Please re-login")
        } else {
            val error = response.errorBody()?.string()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("message"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Error code : ${response.code()}")
            throw ApiException(message.toString())
        }

    }
}
package com.tekzee.amiggos.ui.storieviewnew

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.StorieRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.storieview.StorieEvent
import com.tekzee.amiggos.util.*

import java.lang.Exception

class StorieViewModel(private val context: Context,
                      private val repository: StorieRepository,
                      private val languageConstant: LanguageData,
                      private val prefs: SharedPreference
) : ViewModel() {

    var storieEvent: StorieEvent? = null


    fun callAcceptDeclineApi(memorieId: String, status: Int) {
        Coroutines.main {
            storieEvent?.onAcceptDeclineCalled()
            try {
                val jsoninput = JsonObject()
                jsoninput.addProperty("userid",prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("memory_id",memorieId)
                jsoninput.addProperty("type",prefs.getValueString(ConstantLib.TYPE))
                jsoninput.addProperty("approval_status",status)
                jsoninput.addProperty("venue_id",prefs.getValueString(ConstantLib.VENUE_ID))
                val response =  repository.docallAcceptDeclineApi(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    storieEvent?.onAcceptDeclineResponse(response.message)
                }else{
                    storieEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                storieEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                storieEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                storieEvent?.sessionExpired(e.message!!)
            }
        }
    }


    fun callDeleteApi(memorieId: String) {
        Coroutines.main {
            storieEvent?.onAcceptDeclineCalled()
            try {
                val jsoninput = JsonObject()
                jsoninput.addProperty("userid",prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("memory_id",memorieId)
                val response =  repository.docallDeleteApi(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    storieEvent?.onDeleteResponse(response.message)
                }else{
                    storieEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                storieEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                storieEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                storieEvent?.sessionExpired(e.message!!)
            }
        }
    }


}

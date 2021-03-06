package com.tekzee.amiggos.ui.storieviewnew

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.StorieRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
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
                jsoninput.addProperty("our_story_id",memorieId)
                jsoninput.addProperty("sender_id",memorieId)
                val response =  repository.rejectOurStory(jsoninput, Utility.createHeaders(prefs))
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

    fun callBannerCountApi(
        memorieId: String,
        listItem: MemorieResponse.Data.Memories.Memory.Tagged,
        featured_brand_id: String
    ) {
        Coroutines.main {
            storieEvent?.onAcceptDeclineCalled()
            try {
                val jsoninput = JsonObject()
                jsoninput.addProperty("userid",prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("brand_id",featured_brand_id)
                jsoninput.addProperty("usertype",prefs.getValueString(ConstantLib.USER_TYPE))
                jsoninput.addProperty("memory_id",memorieId)
                val response =  repository.doBannerCountApi(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    storieEvent?.onBannerCountSuccess(response.message,listItem)
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


    fun callDeleteApi(memorieId: String, counter: Int) {
        Coroutines.main {
            storieEvent?.onAcceptDeclineCalled()
            try {
                val jsoninput = JsonObject()
                jsoninput.addProperty("userid",prefs.getValueInt(ConstantLib.USER_ID))
                jsoninput.addProperty("memory_id",memorieId)
                if(prefs.getValueString(ConstantLib.TYPEFROM).equals(ConstantLib.MEMORIES)){
                    jsoninput.addProperty("type","1")
                }else if(prefs.getValueString(ConstantLib.TYPEFROM).equals(ConstantLib.OURMEMORIES)){
                    jsoninput.addProperty("type","2")
                }

                val response =  repository.docallDeleteApi(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    storieEvent?.onDeleteResponse(response.message,counter)
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


    fun callRejectPartyInviteApi(jsoninput: JsonObject) {
        Coroutines.main {
            storieEvent?.onAcceptDeclineCalled()
            try {
                val response =  repository.doRejectCreateMemoryInvitationApi(jsoninput, Utility.createHeaders(prefs))
                if(response.status){
                    storieEvent?.onJoinMemoryRejectedSuccess(response.message)
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

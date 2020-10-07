package com.tekzee.amiggos.ui.finalbasket

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.FinalBasketRepository
import com.tekzee.amiggos.util.*
import java.lang.Exception

class FinalBasketViewModel(private val context: Context,
                           private val repository: FinalBasketRepository,
                           private val languageConstant: LanguageData,
                           private val prefs: SharedPreference
) : ViewModel() {

    var finalBasketEvent: FinalBasketEvent? = null

    fun callCreateBookingApi(input: JsonObject?) {
         Coroutines.main {
            finalBasketEvent?.onStarted()
            try {
                val response =  repository.callCreateBooking(input!!, Utility.createHeaders(prefs))
                if(response.status){
                    finalBasketEvent?.onCreateBookingSuccess(response)
                }else{
                    finalBasketEvent?.onCreateBookingFailure(response.message)
                }
            }catch (e: ApiException) {
                finalBasketEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                finalBasketEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                finalBasketEvent?.sessionExpired(e.message!!)
            }
        }
    }

//    private fun docallStaffByIdApi(input: JsonObject) {
//        Coroutines.main {
//            finalBasketEvent?.onStarted()
//            try {
//                val response =  repository.doStaffByIdApi(input, Utility.createHeaders(prefs))
//                if(response.status){
//                    commonlist.value = response.data.staffList
//                    finalBasketEvent?.onLoaded()
//                }else{
//                    finalBasketEvent?.onFailure(response.message)
//                }
//            }catch (e: ApiException) {
//                finalBasketEvent?.onFailure(e.message!!)
//            } catch (e: NoInternetException) {
//                finalBasketEvent?.onFailure(e.message!!)
//            }catch (e: Exception) {
//                finalBasketEvent?.sessionExpired(e.message!!)
//            }
//        }
//    }

//    fun callChangeUserStatus(
//        item: CommonMenuResponse.Data.Staff,
//        status: Boolean
//    ) {
//        val input = JsonObject()
//        input.addProperty("userid", prefs.getValueString(ConstantLib.USER_ID))
//        if(status){
//            input.addProperty("status", "1")
//        }else{
//            input.addProperty("status", "0")
//        }
//
//        input.addProperty("staff_id", item.id)
//        dochangeuserstatus(input)
//    }

//
//    private fun dochangeuserstatus(input: JsonObject) {
//        Coroutines.main {
//            finalBasketEvent?.onChangeStatusStarted()
//            try {
//                val response =  repository.doChangeUserStaus(input, Utility.createHeaders(prefs))
//                if(response.status){
//                    finalBasketEvent?.onStatusUpdated(response.message)
//                }else{
//                    finalBasketEvent?.onFailure(response.message)
//                }
//            }catch (e: ApiException) {
//                finalBasketEvent?.onFailure(e.message!!)
//            } catch (e: NoInternetException) {
//                finalBasketEvent?.onFailure(e.message!!)
//            }catch (e: Exception) {
//                finalBasketEvent?.sessionExpired(e.message!!)
//            }
//        }
//    }
}

package com.tekzee.amiggos.ui.menu.commonfragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.StaffRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.menu.commonfragment.model.CommonMenuResponse
import com.tekzee.amiggos.util.*
import java.lang.Exception

class CommonViewModel(private val context: Context,
                      private val repository: StaffRepository,
                      private val languageConstant: LanguageData,
                      private val prefs: SharedPreference
) : ViewModel() {

    var commonEvent: CommonEvent? = null
    var commonlist = MutableLiveData<ArrayList<CommonMenuResponse.Data.Staff>>()
    
    fun callStaffByIdApi(categoryId: String?) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueString(ConstantLib.USER_ID))
        input.addProperty("role_id", categoryId)
        docallStaffByIdApi(input)
    }

    private fun docallStaffByIdApi(input: JsonObject) {
        Coroutines.main {
            commonEvent?.onStarted()
            try {
                val response =  repository.doStaffByIdApi(input, Utility.createHeaders(prefs))
                if(response.status){
                    commonlist.value = response.data.staffList
                    commonEvent?.onLoaded()
                }else{
                    commonEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                commonEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                commonEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                commonEvent?.sessionExpired(e.message!!)
            }
        }
    }

    fun callChangeUserStatus(
        item: CommonMenuResponse.Data.Staff,
        status: Boolean
    ) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueString(ConstantLib.USER_ID))
        if(status){
            input.addProperty("status", "1")
        }else{
            input.addProperty("status", "0")
        }

        input.addProperty("staff_id", item.id)
        dochangeuserstatus(input)
    }


    private fun dochangeuserstatus(input: JsonObject) {
        Coroutines.main {
            commonEvent?.onChangeStatusStarted()
            try {
                val response =  repository.doChangeUserStaus(input, Utility.createHeaders(prefs))
                if(response.status){
                    commonEvent?.onStatusUpdated(response.message)
                }else{
                    commonEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                commonEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                commonEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                commonEvent?.sessionExpired(e.message!!)
            }
        }
    }
}

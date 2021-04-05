package com.tekzee.amiggos.ui.addusers

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.AddUserRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.*
import com.tekzee.amiggos.ui.addusers.model.AddUserResponse

class AddUserViewModel(private val context: Context,
                       private val repository: AddUserRepository,
                       private val languageConstant: LanguageData,
                       private val prefs: SharedPreference
) : ViewModel() {
    var event: AddUserEvent? = null
    var staffList = MutableLiveData<ArrayList<AddUserResponse.Data.Staff>>()



    fun onBackButtonClicked(view: View){
        event?.onBackButtonPressed()
    }





    fun CallGetUsers(){
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
        input.addProperty("search", "")
        doCallGetUsersApi(input)
    }

    private fun doCallGetUsersApi(input: JsonObject) {
        Coroutines.main {
            event?.onStarted()
            try {
                val response =  repository.doAddUserApi(input, Utility.createHeaders(prefs))
                if(response.status){
                    staffList.value = response.data.staffList
                    event?.onLoaded(response)
                }else{
                    event?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                event?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                event?.onFailure(e.message!!)
            }catch (e: Exception) {
                event?.sessionExpired(e.message!!)
            }
        }
    }


}
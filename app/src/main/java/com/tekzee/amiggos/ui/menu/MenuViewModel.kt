package com.tekzee.amiggos.ui.menu

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.StaffRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.*

class MenuViewModel(private val context: Context,
                    private val repository: StaffRepository,
                    private val languageConstant: LanguageData,
                    private val prefs: SharedPreference
) : ViewModel() {

    var menuEvent: MenuEvent? = null

    fun onDrawerIconClicked(view: View){
        menuEvent?.onDrawerClicked()
    }


    fun callStaffApi(){
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueString(ConstantLib.USER_ID))
        input.addProperty("venue_id", prefs.getValueString(ConstantLib.VENUE_ID))
        docallStaffApi(input)
    }

    private fun docallStaffApi(input: JsonObject) {
        Coroutines.main {
            menuEvent?.onStarted()
            try {
                val response =  repository.doStaffApi(input, Utility.createHeaders(prefs))
                if(response.status){
                    menuEvent?.onLoaded(response.data.staffList)
                }else{
                    menuEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                menuEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                menuEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                menuEvent?.sessionExpired(e.message!!)
            }
        }
    }
}

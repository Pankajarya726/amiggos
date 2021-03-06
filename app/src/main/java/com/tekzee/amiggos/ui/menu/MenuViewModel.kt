package com.tekzee.amiggos.ui.menu

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.FinalBasketRepository
import com.tekzee.amiggos.base.repository.MenuRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.*
import java.text.SimpleDateFormat

class MenuViewModel(private val context: Context,
                    private val repository: MenuRepository,
                    private val repositoryfinalbasket: FinalBasketRepository,
                    private val languageConstant: LanguageData,
                    private val prefs: SharedPreference
) : ViewModel() {

    var menuEvent: MenuEvent? = null

    fun onDrawerIconClicked(view: View){
        menuEvent?.onDrawerClicked()
    }

    var fmt: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")


    fun callMenuApi(venueid: String?, date: String?, time: String?) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
        input.addProperty("venue_id", venueid)
        input.addProperty("date", date)
        input.addProperty("time", time)
        if(prefs.getValueString(ConstantLib.SELECTED_VENUE_DIN_TOGO).equals(ConstantLib.TOGO)){
            input.addProperty("method", "To-go")
        }else  if(prefs.getValueString(ConstantLib.SELECTED_VENUE_DIN_TOGO).equals(ConstantLib.RESERVATION)){
            input.addProperty("method", "Dine-in")
        }else{
            input.addProperty("method", "")
        }

        doCallMenuApi(input)
    }

    private fun doCallMenuApi(input: JsonObject) {
        Coroutines.main {
            menuEvent?.onStarted()
            try {
                val response =  repository.doCallMenuApi(input, Utility.createHeaders(prefs))
                if(response.status){
                    menuEvent?.onLoaded(response.data.section,response)
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


    fun callCreateBookingApi(input: JsonObject?) {
        Coroutines.main {
            menuEvent?.onStarted()
            try {
                val response =  repositoryfinalbasket.callCreateBooking(input!!, Utility.createHeaders(prefs))
                if(response.status){
                    menuEvent?.onCreateBookingSuccess(response)
                }else{
                    menuEvent?.onCreateBookingFailure(response.message)
                }
            }catch (e: ApiException) {
                menuEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                menuEvent?.onFailure(e.message!!)
            }catch (e: java.lang.Exception) {
                menuEvent?.sessionExpired(e.message!!)
            }
        }
    }
}

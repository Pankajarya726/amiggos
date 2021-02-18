package com.tekzee.amiggos.ui.invitationlist

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.repository.InvitationListRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.invitationlist.model.InvitationListResponse
import com.tekzee.amiggos.util.*


class InvitationViewModel(private val context: Context,
                          private val repository: InvitationListRepository,
                          private val prefs: SharedPreference
) : ViewModel() {
    var event: InvitationListEvent? = null
    var staffList = MutableLiveData<ArrayList<InvitationListResponse.Data>>()



    fun onBackButtonClicked(view: View){
        event?.onBackButtonPressed()
    }





    fun CallInvitationList(booking_id: String?) {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID).toString())
        input.addProperty("bookingid",booking_id)
        DoCallInvitationList(input)
    }

    private fun DoCallInvitationList(input: JsonObject) {
        Coroutines.main {
            event?.onStarted()
            try {
                val response =  repository.doCallInvitationList(input, Utility.createHeaders(prefs))
                if(response.status){
                    staffList.value = response.data
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
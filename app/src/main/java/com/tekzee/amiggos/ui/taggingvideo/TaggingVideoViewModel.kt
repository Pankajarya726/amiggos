package com.tekzee.amiggos.ui.taggingvideo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.TaggingRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.*
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingEvent
import com.tekzee.amiggosvenueapp.ui.tagging.model.TaggingResponse
import java.lang.Exception

class TaggingVideoViewModel(private val context: Context,
                            private val repository: TaggingRepository,
                            private val languageConstant: LanguageData,
                            private val prefs: SharedPreference
) : ViewModel() {


    var taggingEvent: TaggingEvent? = null
    var taggingList = MutableLiveData<ArrayList<TaggingResponse.Data.Search>>()

    fun callTaggingApi(search_txt:String){
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
        input.addProperty("search", search_txt)
        docallTaggingApi(input)
    }

    private fun docallTaggingApi(input: JsonObject) {
        Coroutines.main {
            taggingEvent?.onStarted()
            try {
                val response =  repository.doTaggingApi(input, Utility.createHeaders(prefs))
                if(response.status){
                    taggingList.value = response.data.search
                    taggingEvent?.onLoaded()
                }else{
                    taggingList.value = response.data.search
                    taggingEvent?.onFailure(response.message)
                }
            }catch (e: ApiException) {
                taggingEvent?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                taggingEvent?.onFailure(e.message!!)
            }catch (e: Exception) {
                taggingEvent?.sessionExpired(e.message!!)
            }
        }
    }
}

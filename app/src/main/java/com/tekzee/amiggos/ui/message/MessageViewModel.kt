package com.tekzee.amiggos.ui.message

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ChatRepository
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.message.model.MyConversation


class MessageViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val languageConstant: LanguageData,
    private val prefs: SharedPreference
) : ViewModel() {


    var messageEvent: MessageEvent? = null
    fun onDrawerIconClicked(view: View) {
        messageEvent?.onDrawerClicked()
    }



    fun getAllMyConversation(myId:String): MutableLiveData<List<MyConversation>> {
        return repository.getMyAllConversation(myId)
    }

    fun updateUserTime() {
        repository.updateUseractiveTime()
    }




}

package com.tekzee.amiggosvenueapp.ui.chat

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ChatRepository
import com.tekzee.amiggos.base.repository.NotificationRepository
import com.tekzee.amiggos.ui.chatnew.ChatEvent
import com.tekzee.amiggos.ui.message.model.Message
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility

class ChatViewModel(
    private val context: Context,
    private val repository: ChatRepository,
    private val notificatRepository: NotificationRepository,
    private val languageConstant: LanguageData,
    private val prefs: SharedPreference
) : ViewModel() {


    var chatEvent: ChatEvent? = null

    fun onBackButtonClicked(view:View){
        chatEvent?.onBackButtonPressed()
    }


    fun doGetChatBetweenSenderAndReceiver(roomId : String): MutableLiveData<ArrayList<Message>> {
        return repository.getChatBetweenSenderAndReceiver(roomId)
    }


    fun deleteSelectedMessage(messageId : String)  {
        return repository.deleteMessageId(messageId)
    }

    fun setMessageStatusToSeen(sender:String,receiver:String) {
        repository.setMessageStatusToSeen(sender,receiver)
    }

    fun removeListener() {
        repository.removeListener()
    }


    fun sendNotification(input: JsonObject){
        Coroutines.main {
            val response =  notificatRepository.sendNotification(input, Utility.createHeaders(prefs))
        }


    }



}

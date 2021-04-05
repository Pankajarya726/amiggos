package com.tekzee.amiggos.ui.chatnew

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ChatRepository
import com.tekzee.amiggos.base.repository.NotificationRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class ChatViewModelFactory(private val context: Context,
                           private val repository: ChatRepository,
                           private val notificatRepository: NotificationRepository,
                           private val languageConstant: LanguageData,
                           private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(context,repository,notificatRepository,languageConstant,prefs) as T
    }
}
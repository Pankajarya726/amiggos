package com.tekzee.amiggos.ui.message

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ChatRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class MessagesViewModelFactory(private val context: Context,
                               private val repository: ChatRepository,
                               private val languageConstant: LanguageData,
                               private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageViewModel(context,repository,languageConstant,prefs) as T
    }
}
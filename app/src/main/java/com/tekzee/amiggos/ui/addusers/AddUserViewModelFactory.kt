package com.tekzee.amiggos.ui.addusers

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.AddUserRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class AddUserViewModelFactory(private val context: Context,
                              private val repository: AddUserRepository,
                              private val languageConstant: LanguageData,
                              private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddUserViewModel(context,repository,languageConstant,prefs) as T
    }
}
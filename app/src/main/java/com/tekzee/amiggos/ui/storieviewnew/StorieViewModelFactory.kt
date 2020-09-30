package com.tekzee.amiggos.ui.storieviewnew

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.StorieRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class StorieViewModelFactory(private val context: Context,
                             private val repository: StorieRepository,
                             private val languageConstant: LanguageData,
                             private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StorieViewModel(context,repository,languageConstant,prefs) as T
    }
}
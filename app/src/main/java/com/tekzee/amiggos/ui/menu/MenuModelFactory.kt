package com.tekzee.amiggos.ui.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.MenuRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class MenuModelFactory(private val context: Context,
                       private val repository: MenuRepository,
                       private val languageConstant: LanguageData,
                       private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MenuViewModel(context,repository,languageConstant,prefs) as T
    }
}
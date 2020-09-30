package com.tekzee.amiggos.ui.memories.mymemories

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.MemorieFeaturedBrandRepository
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class MyMemorieViewModelFactory(private val context: Context,
                                private val frepository: MemorieFeaturedBrandRepository,
                                private val languageConstant: LanguageData,
                                private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyMemoriesViewModel(context,frepository,languageConstant,prefs) as T
    }
}
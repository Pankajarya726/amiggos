package com.tekzee.amiggos.ui.tagging

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.TaggingRepository
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggosvenueapp.ui.tagging.TaggingViewModel

@Suppress("UNCHECKED_CAST")
class TaggingViewModelFactory(private val context: Context,
                              private val repository: TaggingRepository,
                              private val languageConstant: LanguageData,
                              private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaggingViewModel(context,repository,languageConstant,prefs) as T
    }
}
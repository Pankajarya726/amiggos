package com.tekzee.amiggos.ui.finalbasket

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.FinalBasketRepository
import com.tekzee.amiggos.util.SharedPreference

@Suppress("UNCHECKED_CAST")
class FinalBasketViewModelFactory(private val context: Context,
                                  private val repository: FinalBasketRepository,
                                  private val languageConstant: LanguageData,
                                  private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FinalBasketViewModel(context,repository,languageConstant,prefs) as T
    }
}
package com.tekzee.amiggos.ui.invitationlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tekzee.amiggos.base.repository.InvitationListRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.SharedPreference


@Suppress("UNCHECKED_CAST")
class InvitationListViewModelFactory(private val context: Context,
                                     private val repository: InvitationListRepository,
                                     private val prefs: SharedPreference
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InvitationViewModel(context,repository,prefs) as T
    }
}
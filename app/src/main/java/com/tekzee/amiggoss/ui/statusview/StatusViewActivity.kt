package com.tekzee.amiggoss.ui.statusview

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.StatusViewBinding
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib

class StatusViewActivity: BaseActivity() {


    lateinit var binding: StatusViewBinding
    private var languageData: LanguageData? = null
    private var sharedPreferences: SharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.status_view)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViewData()
    }

    private fun setupViewData() {
        binding.txtStatusKey.text = languageData!!.klStatus+" : "
        binding.txtStatus.text = languageData!!.klPending
        binding.txtStatusTitle.text = languageData!!.klWaitingforAdminApproval
        binding.txtStatusDesciption.text =  languageData!!.klApprovalMsg1
    }


    override fun validateError(message: String) {

    }
}
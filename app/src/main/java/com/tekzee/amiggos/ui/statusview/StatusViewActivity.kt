//package com.tekzee.amiggos.ui.statusview
//
//import android.os.Bundle
//import androidx.databinding.DataBindingUtil
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.databinding.StatusViewBinding
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.base.model.LanguageData
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.constant.ConstantLib
//
//class StatusViewActivity: BaseActivity() {
//
//
//    lateinit var binding: StatusViewBinding
//    private var languageData: LanguageData? = null
//    private var sharedPreferences: SharedPreference? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.status_view)
//        sharedPreferences = SharedPreference(this)
//        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
//        setupViewData()
//    }
//
//    private fun setupViewData() {
//        binding.txtStatusKey.text = languageData!!.klStatus+" : "
//        binding.txtStatus.text = languageData!!.klPending
//        binding.txtStatusTitle.text = languageData!!.klWaitingforAdminApproval
//        binding.txtStatusDesciption.text =  languageData!!.klApprovalMsg1
//    }
//
//
//    override fun validateError(message: String) {
//
//    }
//}
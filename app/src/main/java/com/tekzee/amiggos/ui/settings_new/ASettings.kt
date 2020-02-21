package com.tekzee.amiggos.ui.settings_new

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ASettingsBinding
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib

class ASettings: BaseActivity(),ASettingsPresenter.ASettingsPresenterMainView {


    lateinit var binding: ASettingsBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_settings)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }

    override fun validateError(message: String) {
    }
}
package com.tekzee.amiggos.ui.settings_new

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ASettingsBinding
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.settings.SettingsActivity
import com.tekzee.amiggos.util.SharedPreference
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
        setupView()
        setupClickListener()

    }

    private fun setupView() {
        binding.txtName.text = sharedPreference!!.getValueString(ConstantLib.USER_NAME)
        binding.age.text = sharedPreference!!.getValueString(ConstantLib.USER_AGE)
        binding.address.text = sharedPreference!!.getValueString(ConstantLib.USER_DATA)
        Glide.with(this).load(sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
            .placeholder(R.drawable.user)
            .into(binding.imgUser)
    }

    private fun setupClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }

        binding.settings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java);
            startActivity(intent)
        }

        binding.nLogout.setOnClickListener{
            val pDialog = SweetAlertDialog(this)
            pDialog.titleText = languageData!!.klLogoutConfirm
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
            }
            pDialog.setConfirmButton(languageData!!.klOk) {
                pDialog.dismiss()
                FirebaseAuth.getInstance().signOut()
                sharedPreference!!.clearSharedPreference()
                val intent = Intent(this, MainSplashActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            pDialog.show()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }

    override fun validateError(message: String) {
    }
}
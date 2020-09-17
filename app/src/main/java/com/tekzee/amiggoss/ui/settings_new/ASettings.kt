package com.tekzee.amiggoss.ui.settings_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ASettingsBinding
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.ui.blockedusers.ABlockedUser
import com.tekzee.amiggoss.ui.chooselanguage.ChooseLanguageActivity
import com.tekzee.amiggoss.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggoss.ui.newpreferences.ANewPreferences
import com.tekzee.amiggoss.ui.profiledetails.AProfileDetails
import com.tekzee.amiggoss.ui.settings.SettingsActivity
import com.tekzee.amiggoss.ui.stripepayment.APaymentMethod
import com.tekzee.amiggoss.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggoss.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib

class ASettings : BaseActivity(), ASettingsPresenter.ASettingsPresenterMainView {


    private lateinit var asettingspresenterimplementation: ASettingsPresenterImplementation
    var binding: ASettingsBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_settings)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        asettingspresenterimplementation =  ASettingsPresenterImplementation(this,this)
        setupClickListener()
        callGetProfileApi()
        setupLanguage()

    }

    private fun callGetProfileApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        asettingspresenterimplementation.doCallUserPrfolie(input, Utility.createHeaders(sharedPreference))

    }

    private fun setupLanguage() {
        binding!!.txtAge.text = languageData!!.PAGE
        binding!!.txtLifestylePreference.text = languageData!!.PLIFESTYLEPREFERENCE
        binding!!.editProfile.text = languageData!!.PEDITPROFILE
        binding!!.paymentMethod.text = languageData!!.PPAYMENTMETHOD
        binding!!.settings.text = languageData!!.PNOTIFICATIONANDSETTINS
        binding!!.blockeduser.text = languageData!!.PBLOCKEDUSERS
        binding!!.changeLangauge.text = languageData!!.PCHANGELANGUAGE
        binding!!.myId.text = languageData!!.PMYID
        binding!!.nLogout.text = languageData!!.PLOGOUT
        binding!!.viewprofile.text = languageData!!.PVIEWPROFILE
    }

    private fun setupView(data: GetUserProfileResponse.Data) {
        binding!!.txtName.text = data.name+" "+data.lastName
        binding!!.age.text = data.age
        binding!!.address.text = data.address
        Glide.with(this).load(data.profile)
            .placeholder(R.drawable.user)
            .into(binding!!.imgUser)
    }

    private fun setupClickListener() {
        binding!!.imgClose.setOnClickListener {
            onBackPressed()
        }

        binding!!.paymentMethod.setOnClickListener{
            val intent = Intent(this, APaymentMethod::class.java);
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding!!.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java);
            startActivity(intent)
        }

        binding!!.viewprofile.setOnClickListener {
            val intent = Intent(this, AProfileDetails::class.java)
            intent.putExtra(ConstantLib.FRIEND_ID, sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
            intent.putExtra(ConstantLib.PROFILE_IMAGE, sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
            startActivity(intent)
            Animatoo.animateSlideRight(this)
        }

        binding!!.blockeduser.setOnClickListener {
            val intent = Intent(this, ABlockedUser::class.java)
            startActivity(intent)
        }


        binding!!.txtLifestylePreference.setOnClickListener {
            val intent = Intent(this, ANewPreferences::class.java)
            startActivity(intent)
        }




        binding!!.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding!!.changeLangauge.setOnClickListener {
            val intent = Intent(this, ChooseLanguageActivity::class.java)
            startActivity(intent)
        }

        binding!!.editProfile.setOnClickListener {
            val intent = Intent(this, AViewAndEditProfile::class.java)
            intent.putExtra(ConstantLib.FROM, "EDIT")
            startActivity(intent)
            Animatoo.animateSlideRight(this)
        }

        binding!!.nLogout.setOnClickListener {
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

    override fun onUserProfileSuccess(data: GetUserProfileResponse.Data) {
        setupView(data)
    }

    override fun onUserProfileFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        asettingspresenterimplementation.onStop()
    }
}
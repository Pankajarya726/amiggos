package com.tekzee.amiggos.ui.settings_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ASettingsBinding
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.blockedusers.ABlockedUser
import com.tekzee.amiggos.ui.chooselanguage.ChooseLanguageActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.newpreferences.ANewPreferences
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.settings.SettingsActivity
import com.tekzee.amiggos.ui.stripepayment.APaymentMethod
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility

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
        asettingspresenterimplementation =  ASettingsPresenterImplementation(this, this)
        setupClickListener()
        setupLanguage()

    }

    override fun onResume() {
        super.onResume()
        callGetProfileApi()
    }

    private fun callGetProfileApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        asettingspresenterimplementation.doCallUserPrfolie(
            input, Utility.createHeaders(
                sharedPreference
            )
        )

    }

    private fun setupLanguage() {
        binding!!.txtAge.text = languageData!!.page
        binding!!.txtLifestylePreference.text = languageData!!.plifestylepreference
        binding!!.editProfile.text = languageData!!.peditprofile
        binding!!.paymentMethod.text = languageData!!.ppaymentmethod
        binding!!.settings.text = languageData!!.pnotificationandsettins
        binding!!.blockeduser.text = languageData!!.pblockeduser
        binding!!.changeLangauge.text = languageData!!.pchangelanguage
        binding!!.myId.text = languageData!!.pmyid
        binding!!.nLogout.text = languageData!!.plogout
        binding!!.viewprofile.text = languageData!!.pviewprofile
        binding!!.txtEditPhoto.text = languageData!!.editprofile
    }

    private fun setupView(data: GetUserProfileResponse.Data) {
        sharedPreference!!.save(ConstantLib.ISPROFILECOMPLETE, data.isProfileComplete)
        binding!!.txtName.text = data.name+" "+data.lastName
        binding!!.age.text = data.age.toString()
        binding!!.address.text = data.city+", "+data.state
        Glide.with(this).load(data.profile)
            .placeholder(R.drawable.noimage)
            .into(binding!!.imgUser)
    }

    private fun setupClickListener() {
        binding!!.imgClose.setOnClickListener {
            onBackPressed()
        }
        binding!!.imgMail.setOnClickListener {

            try {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
                sendIntent.type = "text/plain"
                startActivity(sendIntent)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding!!.myId.setOnClickListener {
            startActivity(Intent(applicationContext, AttachIdActivity::class.java))
        }

        binding!!.paymentMethod.setOnClickListener{
            val intent = Intent(this, APaymentMethod::class.java);
            intent.putExtra(ConstantLib.FROM, ConstantLib.PAYMENTMETHOD)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }

        binding!!.settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java);
            startActivity(intent)
        }

        binding!!.viewprofile.setOnClickListener {
            val intent = Intent(this, AProfileDetails::class.java)
            intent.putExtra(
                ConstantLib.FRIEND_ID,
                sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString()
            )
            intent.putExtra(
                ConstantLib.PROFILE_IMAGE,
                sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE)
            )
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

        binding!!.txtEditPhoto.setOnClickListener {
            val intent = Intent(this, AViewAndEditProfile::class.java)
            intent.putExtra(ConstantLib.FROM, "EDIT")
            intent.putExtra(ConstantLib.NAME, binding!!.txtName.text.toString().trim())
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
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        asettingspresenterimplementation.onStop()
    }
}
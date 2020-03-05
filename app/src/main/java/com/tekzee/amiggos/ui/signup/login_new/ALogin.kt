package com.tekzee.amiggos.ui.signup.login_new

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.LoginNewBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.signup.login_new.model.ALoginResponse
import com.tekzee.amiggos.ui.signup.stepone.StepOne
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class ALogin: BaseActivity(), ALoginPresenter.ALoginPresenterMainView {

    private var binding: LoginNewBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aLoginImplementation: ALoginImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_new)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aLoginImplementation = ALoginImplementation(this,this)
        setupClickListener()
        setupLanguage()
    }

    private fun setupLanguage() {
        binding!!.lusername.hint = languageData!!.PUSERNAME
        binding!!.lpassword.hint = languageData!!.PPASSWORD
        binding!!.btnSignin.text = languageData!!.PSIGNIN
        binding!!.btnCreateaccount.text = languageData!!.PCREATEANACCOUNT
    }

    private fun setupClickListener() {
        binding!!.btnCreateaccount.setOnClickListener{

            startActivity(Intent(applicationContext,StepOne::class.java))
            Animatoo.animateSlideLeft(this)
        }
        binding!!.btnSignin.setOnClickListener{
            if(validateFields()){
                callLoginApi()
            }
        }
    }

    private fun callLoginApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("username", binding!!.lusername.text.toString().trim())
        input.addProperty("password", binding!!.lpassword.text.toString().trim())
        input.addProperty("device_type", ConstantLib.DEVICETYPE)
        input.addProperty("device_id", sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN))
        aLoginImplementation!!.doCallLoginApi(input, Utility.createHeaders(sharedPreferences))
    }

    private fun validateFields(): Boolean {
        if(binding!!.lusername.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Username can not be blank",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.lpassword.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Password can not be blank",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun OnLoginSuccess(responseData: ALoginResponse.Data) {
        sharedPreferences!!.save(ConstantLib.USER_ID, responseData.userid.toInt())
        sharedPreferences!!.save(ConstantLib.USER_NAME, responseData.username)
        sharedPreferences!!.save(ConstantLib.USER_EMAIL, responseData.email)
        sharedPreferences!!.save(ConstantLib.USER_DOB, responseData.dob)
        sharedPreferences!!.save(ConstantLib.API_TOKEN, responseData.apiToken)
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, responseData.profile)
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        startActivity(Intent(applicationContext, AHomeScreen::class.java))
        finishAffinity()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
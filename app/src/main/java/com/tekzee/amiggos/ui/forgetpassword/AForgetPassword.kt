package com.tekzee.amiggos.ui.forgetpassword

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ForgetPasswordBinding
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility

class AForgetPassword: BaseActivity(), AForgetPasswordPresenter.AForgetPasswordPresenterView {

    private var binding: ForgetPasswordBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aLoginImplementation: AForgetPasswordImplementation? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.forget_password)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aLoginImplementation = AForgetPasswordImplementation(this,this)
        setupClickListener()
    }


    private fun setupClickListener() {
        binding!!.btnBack.setOnClickListener{
            onBackPressed()
        }
        binding!!.btnSubmit.setOnClickListener{
            if(validateFields()){
                callForgetPasswordApi()
            }
        }
    }

    private fun callForgetPasswordApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("email", binding!!.edtEmail.text.toString())
        input.addProperty("device_type", ConstantLib.DEVICETYPE)
        aLoginImplementation!!.callForgetPasswordApi(input, Utility.createHeaders(sharedPreferences))
    }

    private fun validateFields(): Boolean {
        if(binding!!.edtEmail.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Email can not be blank",Toast.LENGTH_LONG).show()
            return false
        }else if(!Utility.isEmailValid(binding!!.edtEmail.text.toString().trim())){
            Toast.makeText(applicationContext,"Invalid email",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun OnForgetPasswordSuccess(responseData: String) {
        Toast.makeText(applicationContext,responseData,Toast.LENGTH_LONG).show()
        finish()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
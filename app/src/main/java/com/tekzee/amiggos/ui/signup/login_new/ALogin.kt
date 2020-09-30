package com.tekzee.amiggos.ui.signup.login_new

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.LoginNewBinding
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.signup.login_new.model.ALoginResponse
import com.tekzee.amiggos.ui.signup.stepone.StepOne
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.forgetpassword.AForgetPassword

class ALogin: BaseActivity(), ALoginPresenter.ALoginPresenterMainView {

    private var binding: LoginNewBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aLoginImplementation: ALoginImplementation? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_new)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aLoginImplementation = ALoginImplementation(this,this)
        setupClickListener()
        setupLanguage()
    }

    private fun setupLanguage() {
        binding!!.lusername.hint = languageData!!.pusername
        binding!!.lpassword.hint = languageData!!.ppassword
        binding!!.btnSignin.text = languageData!!.psignin
        binding!!.btnCreateaccount.text = languageData!!.pcreateanaccount
    }

    private fun setupClickListener() {
        binding!!.btnCreateaccount.setOnClickListener{

            startActivity(Intent(applicationContext,StepOne::class.java))
            Animatoo.animateSlideLeft(this)
        }

        binding!!.btnForgetpassword.setOnClickListener {
            startActivity(Intent(applicationContext,AForgetPassword::class.java))
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

        checkIfFirebaseUserExist(responseData)

    }

    private fun checkIfFirebaseUserExist(responseData: ALoginResponse.Data) {
        val auth:FirebaseAuth = FirebaseAuth.getInstance()
        val email = responseData.userid.toString().toLowerCase()+"_1" + "@amiggos.com"
        auth.signInWithEmailAndPassword(email, "amiggos@123")
            .addOnCompleteListener(this) { task: Task<AuthResult> ->

                if(task.isSuccessful){
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        updateFirebaseUser(responseData)
                        callUpdateFirebaseApi(responseData.userid)
                        callHomePage(responseData)
                    }else{
                        auth.createUserWithEmailAndPassword(email, "amiggos@123")
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    createFirebaseUser(responseData)
                                    callUpdateFirebaseApi(responseData.userid)
                                    callHomePage(responseData)
                                }else{
                                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Error login for chat module")
                                        .setConfirmText(languageData!!.klOk)
                                        .setConfirmClickListener { sDialog ->
                                            sDialog.dismissWithAnimation()
                                        }
                                        .show()
                                }
                            }
                    }
                }else{
                    auth.createUserWithEmailAndPassword(email, "amiggos@123")
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                createFirebaseUser(responseData)
                                callUpdateFirebaseApi(responseData.userid)
                                callHomePage(responseData)
                            }else{
                                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Error login for chat module")
                                    .setConfirmText(languageData!!.klOk)
                                    .setConfirmClickListener { sDialog ->
                                        sDialog.dismissWithAnimation()
                                    }
                                    .show()
                            }
                        }
//                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText(task.exception!!.message)
//                        .setConfirmText(languageData!!.klOk)
//                        .setConfirmClickListener { sDialog ->
//                            sDialog.dismissWithAnimation()
//                        }
//                        .show()
                }


            }
    }

    private fun updateFirebaseUser(responseData: ALoginResponse.Data) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        database.child(ConstantLib.USER).child(firebaseUser!!.uid).child("name").setValue(responseData.name)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("image").setValue(responseData.profile)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("deviceToken").setValue(responseData.apiToken)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("deviceToken").setValue(responseData.apiToken)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("amiggosID").setValue(responseData.userid.toString())
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("email").setValue(responseData.email)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("fcmToken").setValue(sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN))

    }

    private fun callHomePage(responseData: ALoginResponse.Data) {
        sharedPreferences!!.save(ConstantLib.USER_ID, responseData.userid.toInt())
        sharedPreferences!!.save(ConstantLib.USER_NAME, responseData.username)
        sharedPreferences!!.save(ConstantLib.USER_EMAIL, responseData.email)
        sharedPreferences!!.save(ConstantLib.USER_DOB, responseData.dob)
        sharedPreferences!!.save(ConstantLib.API_TOKEN, responseData.apiToken)
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, responseData.profile)
        sharedPreferences!!.save(ConstantLib.USER_TYPE, responseData.type)
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        sharedPreferences!!.save(ConstantLib.ISPROFILECOMPLETE, responseData.is_profile_complete)
        startActivity(Intent(applicationContext, AHomeScreen::class.java))
        finishAffinity()
    }

    override fun onFirebaseUpdateSuccess(responseData: LoginResponse) {
        Log.d("Firebase id updated","Updated")
    }


    private fun callUpdateFirebaseApi(userid: Int) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", userid.toString())
        input.addProperty("firebase_id", FirebaseAuth.getInstance().currentUser!!.uid)
        aLoginImplementation!!.doUpdateFirebaseApi(input, Utility.createHeaders(sharedPreferences))

    }





    private fun createFirebaseUser(responseData: ALoginResponse.Data) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val user = User()
        user.amiggosID = responseData.userid.toString()
        user.deviceToken = responseData.apiToken
        user.email = responseData.email
        user.fcmToken = sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString()
        user.image = responseData.profile
        user.name = responseData.name
        database.child(ConstantLib.USER).child(firebaseUser!!.uid).setValue(user)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
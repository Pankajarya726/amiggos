package com.tekzee.amiggos.ui.mainsplash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.MainSplashActivityBinding
import com.tekzee.amiggos.ui.agegroup.AgeGroupActivity
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.amiggos.ui.login.LoginActivity
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.splash.SplashActivity
import com.tekzee.amiggos.ui.statusview.StatusViewActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib


class MainSplashActivity : BaseActivity(), MainSplashPresenter.MainSplashPresenterMainView {

    private var validateAppVersionResponse: ValidateAppVersionResponse? = null
    lateinit var binding: MainSplashActivityBinding
    private var mainSplashPresenterImplementation: MainSplashPresenterImplementation? = null
    private var sharedPreferences: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_splash_activity);
        mainSplashPresenterImplementation = MainSplashPresenterImplementation(this, this)
        sharedPreferences = SharedPreference(this)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this) { instanceIdResult ->
            sharedPreferences!!.save(ConstantLib.FCMTOKEN,instanceIdResult.token)
        }

        callValidateAppVersionApi()


        if(BuildConfig.DEBUG){
            Utility.getShaKey(applicationContext)
        }


    }



    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun callValidateAppVersionApi() {
        val input = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("version", BuildConfig.VERSION_NAME)
        mainSplashPresenterImplementation!!.doValidateAppVersionApi(input,Utility.createHeaders(sharedPreferences))
    }

    private fun callLanguageConstantApi() {
        mainSplashPresenterImplementation!!.doLanguageConstantApi(Utility.createHeaders(sharedPreferences))
    }


    override fun onValidateAppVersionResponse(response: ValidateAppVersionResponse) {

        validateAppVersionResponse = response
        callLanguageConstantApi()


    }


    override fun onLanguageConstantResponse(response: JsonObject) {

        sharedPreferences!!.save(ConstantLib.LANGUAGE_DATA,response.toString())

        when(validateAppVersionResponse!!.update_type){
            1->{
                compulsaryUpdation(validateAppVersionResponse!!.message)
            }
            2->{
                optionalUpdation(validateAppVersionResponse!!.message)
            }
        }

        if(validateAppVersionResponse!!.data != null){
            checkValidateVersionPerformAction(validateAppVersionResponse)
        }



    }

    private fun optionalUpdation(message: String) {

    }

    private fun compulsaryUpdation(message: String) {

    }

    private fun checkValidateVersionPerformAction(validateAppVersionResponse: ValidateAppVersionResponse?) {
        if(validateAppVersionResponse!!.data.age.isNotEmpty()){
            sharedPreferences!!.save(ConstantLib.USER_AGE, validateAppVersionResponse.data.age)
        }
        if(validateAppVersionResponse.data.invite_friend_count!=null && validateAppVersionResponse.data.invite_friend_count.isNotEmpty() && validateAppVersionResponse.data.invite_friend_count.toInt()>0){
            sharedPreferences!!.save(ConstantLib.INVITE_FRIEND_COUNT, validateAppVersionResponse.data.age)
        }

        when(validateAppVersionResponse.data.status){
            "0"->{
                Logger.d("Login failed")
                showSplashScreen()
            }
            "1"->{
                showAttachedIDController()
            }
            "2"->{
                showApprovalController()
            }
            "3"->{
                showSelectAgeGroupScreen()
            }
            "4"->{
                showHomeController()
            }
            "5"->{
                showAttachedIDController()
            }
        }

    }

    private fun showLoginScreen() {
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showSplashScreen() {
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        val intent = Intent(applicationContext, SplashActivity::class.java)
        intent.putExtra(ConstantLib.FROM,"")
        startActivity(intent)
        finish()
    }

    private fun showHomeController() {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showSelectAgeGroupScreen() {
        val intent = Intent(applicationContext, AgeGroupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showApprovalController() {
        val intent = Intent(applicationContext, StatusViewActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAttachedIDController() {
        val intent = Intent(applicationContext, AttachIdActivity::class.java)
        startActivity(intent)
        finish()
    }

}
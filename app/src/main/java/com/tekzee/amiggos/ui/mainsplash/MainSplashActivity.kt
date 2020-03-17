package com.tekzee.amiggos.ui.mainsplash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.facebook.login.Login
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.BuildConfig
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.MainSplashActivityBinding
import com.tekzee.amiggos.ui.agegroup.AgeGroupActivity
import com.tekzee.amiggos.ui.attachid.AttachIdActivity
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.login.LoginActivity
import com.tekzee.amiggos.ui.mainsplash.model.ValidateAppVersionResponse
import com.tekzee.amiggos.ui.splash.SplashActivity
import com.tekzee.amiggos.ui.statusview.StatusViewActivity
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.databinding.StepOneBinding
import com.tekzee.amiggos.ui.signup.login_new.ALogin
import com.tekzee.amiggos.ui.signup.stepone.StepOne
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib


class MainSplashActivity : BaseActivity(), MainSplashPresenter.MainSplashPresenterMainView {
    private var validateAppVersionResponse: ValidateAppVersionResponse? = null
    lateinit var binding: MainSplashActivityBinding
    private var mainSplashPresenterImplementation: MainSplashPresenterImplementation? = null
    private var sharedPreferences: SharedPreference? = null
    private var lastLocation: LatLng? = null
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
        input.addProperty("device_type", "2")
        mainSplashPresenterImplementation!!.doValidateAppVersionApi(input, Utility.createHeaders(sharedPreferences))
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

        when(validateAppVersionResponse!!.updateType){
            0->{
                if(validateAppVersionResponse!!.data != null){
                    checkValidateVersionPerformAction(validateAppVersionResponse)
                }
            }
            1->{
                compulsaryUpdation(validateAppVersionResponse!!.message)
            }
            2->{
                optionalUpdation(validateAppVersionResponse!!.message)
            }

        }




    }

    private fun optionalUpdation(message: String) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = message
        pDialog.setCancelable(false)
        pDialog.setCancelButton("CANCEL") {
            pDialog.dismiss()
            if(validateAppVersionResponse!!.data != null){
                checkValidateVersionPerformAction(validateAppVersionResponse)
            }
        }
        pDialog.setConfirmButton("UPDATE") {
            pDialog.dismiss()
            openPlayStore();
        }
        pDialog.show()
    }

    private fun compulsaryUpdation(message: String) {

        val pDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        pDialog.titleText = message
        pDialog.setCancelable(false)
        pDialog.setConfirmButton("UPDATE") {
            pDialog.dismiss()

            openPlayStore();
        }
        pDialog.show()
    }

    private fun openPlayStore() {
        val appPackageName =
            packageName // getPackageName() from Context or Activity object

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun checkValidateVersionPerformAction(validateAppVersionResponse: ValidateAppVersionResponse?) {
        if(validateAppVersionResponse!!.data.age.isNotEmpty()){
            sharedPreferences!!.save(ConstantLib.USER_AGE, validateAppVersionResponse.data.age)
        }
        if(validateAppVersionResponse.data.inviteFriendCount!=null && validateAppVersionResponse.data.inviteFriendCount.isNotEmpty() && validateAppVersionResponse.data.inviteFriendCount.toInt()>0){
            sharedPreferences!!.save(ConstantLib.INVITE_FRIEND, validateAppVersionResponse.data.inviteFriendCount.toInt())
        }
        sharedPreferences!!.save(ConstantLib.NO_DAY_REGISTER, validateAppVersionResponse.data.noDayRegister)
        sharedPreferences!!.save(
            ConstantLib.IS_INVITE_FRIEND,
            validateAppVersionResponse.data.isFreindInvities.toInt()
        )
//        when(validateAppVersionResponse.data.status){
//            "0"->{
//                Logger.d("Login failed")
//                showSplashScreen()
//            }
//            "1"->{
//                showAttachedIDController()
//            }
//            "2"->{
//                showApprovalController()
//            }
//            "3"->{
//                showSelectAgeGroupScreen()
//            }
//            "4"->{
//                showHomeController()
//            }
//            "5"->{
//                showAttachedIDController()
//            }
//        }


                when(validateAppVersionResponse.data.status){
            "0"->{
                Logger.d("Login failed")
                showSplashScreen()
            }
            "1"->{
                showHomeController()
            }
            "2"->{
                showHomeController()
            }
            "3"->{
                showHomeController()
            }
            "4"->{
                showHomeController()
            }
            "5"->{
                showHomeController()
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
        val intent = Intent(applicationContext, ALogin::class.java)
        intent.putExtra(ConstantLib.FROM,"")
        startActivity(intent)
        finish()
    }

    private fun showHomeController() {
        val intent = Intent(applicationContext, AHomeScreen::class.java)
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
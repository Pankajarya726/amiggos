package com.tekzee.amiggos.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.gson.JsonObject
import com.nikola.jakshic.instagramauth.*
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.enums.SocialLogins
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.HomeActivityBinding
import com.tekzee.amiggos.databinding.LoginActivityBinding
import com.tekzee.amiggos.ui.agreement.LicenceAgreementActivity
import com.tekzee.amiggos.ui.chooselanguage.ChooseLanguageActivity
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.amiggos.ui.invitefriend.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.login.model.LoginResponse
import com.tekzee.amiggos.ui.pages.WebViewActivity
import com.tekzee.amiggos.ui.referalcode.ReferalCodeActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.SimpleCallback
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.util.*


class LoginActivity : BaseActivity(), LoginPresenter.LoginMainView {

    private val CHECK_LICENCE_AGREEMENT: Int = 111
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var socialLogin: SocialLogins = SocialLogins.NONE
    //google variables
    private val RC_SIGN_IN: Int = 100
    private var mGoogleSignInClient: GoogleSignInClient? = null

    //facebook variables
    private var callbackManager: CallbackManager? = null
    lateinit var binding: LoginActivityBinding

    private var iAgreeFlag: Boolean = false

    private var loginPresenterImplementation: LoginPresenterImplementation? = null
    private var lastLocation: LatLng? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        loginPresenterImplementation = LoginPresenterImplementation(this, this)
        setupViewNames()
        setupFacebookLogin()
        setupGoogleLogin()
        setupClickListener()
        startLocationUpdate()

    }

    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(this, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
            }
        })
    }

    private fun setupViewNames() {
        binding.txtLoginTitle.text = languageData!!.klLoginUsingSocial
        binding.btnFacebook.text = languageData!!.klFaceBook
        binding.btnInstagram.text = languageData!!.klInstagram
        binding.btnGoogle.text = languageData!!.klGoogle
        binding.iAgree.text = languageData!!.klIamAgree
        binding.termsServices.text = " " + languageData!!.klTermsServs
        binding.txtSelectLanguage.text = languageData!!.klSelectLanguage
        binding.txtDontPost.text = languageData!!.klDontPostMedia


            if(sharedPreferences!!.getValueBoolean(ConstantLib.ISAGREE, false)){
                binding.radioAgree.setBackgroundResource(R.drawable.check)
            }else{
                binding.radioAgree.setBackgroundResource(R.drawable.uncheck)
            }
    }

    private fun setupInstagramLogin() {

        AuthManager.getInstance().login(this, object : AuthManager.LoginCallback {
            override fun onSuccess() {
                getInstagramUserInfo()
            }

            override fun onError(e: InstagramAuthException) {
                if (e is InstagramAuthAccessDeniedException) {
                    // User denied access, do something...
                }

                if (e is InstagramAuthNetworkOperationException) {
                    // Network problem, do something...
                }
            }
        })

    }

    private fun getInstagramUserInfo() {

        AuthManager.getInstance().getUserInfoAsync(object : AuthManager.Callback<UserInfo> {
            override fun onSuccess(userInfo: UserInfo) {
                Logger.d(userInfo)
                AuthManager.getInstance().logout()
                callLoginApi(
                    userInfo.photoUrl,
                    userInfo.id,
                    "I",
                    "M",
                    userInfo.fullName,
                    "",
                    userInfo.id,
                    sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString(),
                    Utility.getCurrentLanguageCode(sharedPreferences),
                    "",
                    "",
                    sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString()
                )

            }

            override fun onError(e: InstagramAuthException) {
                Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        })

    }


    private fun setupGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private fun setupClickListener() {
        binding.layoutFacebook.setOnClickListener {
            socialLogin = SocialLogins.FACEBOOK
            setUpMap()
        }

        binding.layoutGoogle.setOnClickListener {
            socialLogin = SocialLogins.GOOGLE
            setUpMap()
        }

        binding.layoutInstagram.setOnClickListener {
            socialLogin = SocialLogins.INSTAGRAM
            setUpMap()
        }

        binding.txtSelectLanguage.setOnClickListener {
            val intent = Intent(this, ChooseLanguageActivity::class.java)
            startActivity(intent)
        }

        binding.termsServices.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(ConstantLib.PAGEURL, ConstantLib.TERMSANDCONDITION)
            startActivity(intent)
        }
        binding.iAgree.setOnClickListener {
            sharedPreferences!!.save(ConstantLib.OPENMIDED_CHECKBOX, false)
            sharedPreferences!!.save(ConstantLib.SPREAD_CHECKBOX, false)
            sharedPreferences!!.save(ConstantLib.TURNTUP_CHECKBOX, false)
            val intent = Intent(this, LicenceAgreementActivity::class.java)
            startActivityForResult(intent, CHECK_LICENCE_AGREEMENT)
        }

        binding.radioAgree.setOnClickListener{
            sharedPreferences!!.save(ConstantLib.OPENMIDED_CHECKBOX, false)
            sharedPreferences!!.save(ConstantLib.SPREAD_CHECKBOX, false)
            sharedPreferences!!.save(ConstantLib.TURNTUP_CHECKBOX, false)
            val intent = Intent(this, LicenceAgreementActivity::class.java)
            startActivityForResult(intent, CHECK_LICENCE_AGREEMENT)
        }
    }

    private fun showTermsAndConditionPopup() {

        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(languageData!!.klPlsvisitTermsCondition)
            .setConfirmText(languageData!!.klOk)
            .setConfirmClickListener { sDialog ->
                sDialog.dismissWithAnimation()
            }
            .show()
    }

    private fun setupFacebookLogin() {

        this.callbackManager = CallbackManager.Factory.create();
        binding.btnFb.setPermissions(
            Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"
            )
        );
        binding.btnFb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Logger.d("TAG" + loginResult.accessToken)
                var accessToken = loginResult.accessToken
                val request = GraphRequest.newMeRequest(
                    accessToken
                ) { jsonResponse, _ ->
                    LoginManager.getInstance().logOut()
                    callLoginApi(
                        "https://graph.facebook.com/" + jsonResponse.getString("id") + "/picture?type=large",
                        accessToken.token.toString(),
                        "F",
                        "",
                        jsonResponse.getString("name"),
                        "",
                        jsonResponse.getString("id"),
                        sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString(),
                        Utility.getCurrentLanguageCode(sharedPreferences),
                        "",
                        jsonResponse.getString("email"),
                        sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString()
                    )


                }

                val parameters = Bundle()
                parameters.putString(
                    "fields",
                    "id,birthday,address,about,gender,name,hometown,email"
                )
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "Login cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun callLoginApi(
        image: String, access_token: String, typeoflogin: String,
        gender: String, name: String,
        phone: String, social_id: String,
        device_token: String, language_id: String,
        dob: String, email: String, device_id: String
    ) {

        val input: JsonObject = JsonObject()
        input.addProperty("image", image)
        input.addProperty("access_token", access_token)
        input.addProperty("typeoflogin", typeoflogin)
        input.addProperty("gender", gender)
        input.addProperty("device_type", "2")
        input.addProperty("latitude", lastLocation!!.latitude)
        input.addProperty("name", name)
        input.addProperty("longitude", lastLocation!!.longitude)
        input.addProperty("phone", phone)
        input.addProperty("social_id", social_id)
        input.addProperty("device_token", device_token)
        input.addProperty("language_id", language_id)
        input.addProperty("dob", dob)
        input.addProperty("email", email)
        input.addProperty("device_id", device_id)
        Logger.d(input)
        loginPresenterImplementation!!.doLoginApi(input, Utility.createHeaders(sharedPreferences))
    }

    override fun validateError(message: String) {

        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        AuthManager.getInstance().onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
        } else if (requestCode == CHECK_LICENCE_AGREEMENT) {
            iAgreeFlag = data!!.getBooleanExtra(ConstantLib.LICENCE_AGREEMENT_COMPLETED, false)
            sharedPreferences!!.save(ConstantLib.ISAGREE, iAgreeFlag)
            if(iAgreeFlag){
                binding.radioAgree.setBackgroundResource(R.drawable.check)
            }else{
                binding.radioAgree.setBackgroundResource(R.drawable.uncheck)
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {

        try {

                val account: GoogleSignInAccount? = task!!.getResult(ApiException::class.java)
                // Signed in successfully, show authenticated UI.
                Logger.d(account!!.displayName + "-" + account.id + "-" + account.displayName + "-" + account.email)
                callLoginApi(
                    account.photoUrl.toString(),
                    account.idToken.toString(),
                    "G",
                    "",
                    account.displayName.toString(),
                    "",
                    account.id.toString(),
                    sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString(),
                    Utility.getCurrentLanguageCode(sharedPreferences),
                    "",
                    account.email.toString(),
                    sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN).toString()
                )



        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.d(e.printStackTrace())
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


    public override fun onStop() {
        super.onStop()
    }

    public override fun onResume() {
        super.onResume()
        if (sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_NAME).isNullOrEmpty()) {
            sharedPreferences!!.save(ConstantLib.LANGUAGE_NAME, "English")
        }
        binding.txtSelectLanguage.text =
            sharedPreferences!!.getValueString(ConstantLib.LANGUAGE_NAME)

    }

    override fun onDestroy() {
        super.onDestroy()
        LoginManager.getInstance().logOut();
    }


    fun checkAgrementStatus(): Boolean {
        return sharedPreferences!!.getValueBoolean(ConstantLib.ISAGREE, false)
    }


    override fun onLoginSuccess(responseData: LoginResponse) {

        sharedPreferences!!.save(ConstantLib.USER_ID, responseData.data[0].userid)
        sharedPreferences!!.save(ConstantLib.USER_NAME, responseData.data[0].name)
        sharedPreferences!!.save(ConstantLib.USER_EMAIL, responseData.data[0].email)
        sharedPreferences!!.save(ConstantLib.USER_DOB, responseData.data[0].dob.toString())
        sharedPreferences!!.save(ConstantLib.API_TOKEN, responseData.data[0].api_token)
        sharedPreferences!!.save(ConstantLib.ACCESS_TOKEN, responseData.data[0].access_token)
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, responseData.data[0].profile)
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        sharedPreferences!!.save(ConstantLib.INVITE_FRIEND,responseData.data[0].invite_friend_count.toInt())

        if (responseData.data[0].is_profile == 1) {
            showHomeController()
        } else {
            showNextController()
        }


    }

    private fun showNextController() {
        val intent = Intent(this, ReferalCodeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun showHomeController() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun setUpMap() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showExplanationDialog()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        } else {
            callButtonClick()
        }

    }


    private fun showExplanationDialog() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = getString(R.string.allow_location_permission)
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        pDialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Logger.d("permission was granted, yay! Do the")
                    startLocationUpdate()
                    callButtonClick()
                } else {
                    Logger.d("permission denied, boo! Disable the")
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
                Logger.d("Ignore all other requests")
            }
        }
    }

    private fun callButtonClick() {
        if (lastLocation != null) {
            if (socialLogin == SocialLogins.FACEBOOK) {
                if (checkAgrementStatus()) {
                    binding.btnFb.performClick()
                } else {
                    showTermsAndConditionPopup()
                }
            } else if (socialLogin == SocialLogins.INSTAGRAM) {
                if (checkAgrementStatus()) {
                    setupInstagramLogin()
                } else {
                    showTermsAndConditionPopup()
                }
            } else if (socialLogin == SocialLogins.GOOGLE) {
                if (checkAgrementStatus()) {
                    val signInIntent = mGoogleSignInClient!!.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                } else {
                    showTermsAndConditionPopup()
                }
            }
        } else {
            Toast.makeText(applicationContext,"Unable to fetch your location ,Please try again", Toast.LENGTH_LONG).show()
            startLocationUpdate()
        }
    }

}
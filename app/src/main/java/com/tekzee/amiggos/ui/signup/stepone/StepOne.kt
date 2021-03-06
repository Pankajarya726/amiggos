package com.tekzee.amiggos.ui.signup.stepone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.StepOneBinding
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.ui.signup.StepOneModel
import com.tekzee.amiggos.ui.signup.steptwo.StepTwo
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.firebasemodel.User
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.settings.model.SettingsResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData
import com.tekzee.amiggos.util.Errortoast
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import java.util.*


class StepOne : BaseActivity(), StepOnePresenter.StepOnePresenterMainView,
    DatePickerDialog.OnDateSetListener {

    private var dateOfBirth: String? = ""
    private var binding: StepOneBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var stepOneImplementation: StepOneImplementation? = null
    private lateinit var database: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.step_one)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        stepOneImplementation = StepOneImplementation(this, this)
        setupLanguage()
        setupClickListener()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() {
        binding!!.btnCancelStepOne.setOnClickListener {
            onBackPressed()
        }

        binding!!.btnNextStepOne.setOnClickListener {
            if (validateFields()) {
                callSignUpActivity()
            }
        }



        binding!!.sdateOfBirth.setOnTouchListener { v: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP) {

                val todaysDate = Calendar.getInstance()
                todaysDate.add(Calendar.YEAR, -18)
                SpinnerDatePickerDialogBuilder()
                    .context(this)
                    .callback(this)
                    .showTitle(true)
                    .showDaySpinner(true)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .defaultDate(
                        todaysDate.get(Calendar.YEAR),
                        todaysDate.get(Calendar.MONTH),
                        todaysDate.get(Calendar.DAY_OF_MONTH)
                    )
                    .maxDate(
                        todaysDate.get(Calendar.YEAR),
                        todaysDate.get(Calendar.MONTH),
                        todaysDate.get(Calendar.DAY_OF_MONTH)
                    )
                    .minDate(1900, 0, 1)
                    .build()
                    .show()

            }
            true
        }
    }


    private fun callSignUpActivity() {
        val input = JsonObject()
        input.addProperty("username", binding!!.susername.text.toString().trim())
        input.addProperty("email", binding!!.semail.text.toString().trim())
        input.addProperty("password", binding!!.spassword.text.toString().trim())
        input.addProperty("date_of_birth", dateOfBirth)
        input.addProperty("device_id", sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN))
        input.addProperty("device_type", ConstantLib.DEVICETYPE)
        input.addProperty("latitude", "")
        input.addProperty("longitude", "")
        stepOneImplementation!!.doCallSignupApi(input, Utility.createHeaders(sharedPreferences))
    }

    private fun validateFields(): Boolean {

        if (binding!!.semail.text.toString().trim().isEmpty()) {
            Errortoast(languageData!!.email_can_not_be_blank)
            return false
        }

        if (!Utility.isEmailValid(binding!!.semail.text.toString().trim())) {
            Errortoast(languageData!!.please_provide_valid_email)
            return false
        }

        if (!Utility.checkEmailCharacter(binding!!.semail.text.toString().trim())) {
            Errortoast(languageData!!.please_provide_valid_email)
            return false
        }

        if (binding!!.spassword.text.toString().trim().isEmpty()) {
            Errortoast(languageData!!.password_can_not_blank)
            return false
        }

        if (!Utility.checkMinimumAndMaximumPasswordCharacter(
                binding!!.spassword.text.toString().trim()
            )
        ) {
            Errortoast(languageData!!.password_should_be_greater_than_6_and_smaller_than_16)
            return false
        }

        if (binding!!.sconfirmpassword.text.toString().trim().isEmpty()) {

            Errortoast(languageData!!.confirm_password_can_not_be_blank)
            return false
        }

        if (!Utility.checkMinimumAndMaximumPasswordCharacter(binding!!.sconfirmpassword.text.toString().trim())) {
            Errortoast(languageData!!.confirm_password_should_be_greater_than_6_and_smaller_than_16)
            return false
        }

        if (binding!!.spassword.text.toString()
                .trim()!=binding!!.sconfirmpassword.text.toString().trim()
        ) {
            Errortoast(languageData!!.password_and_confirm_password_can_not_be_blank)
            return false
        }

        if (binding!!.susername.text.toString().trim().isEmpty()) {
            Errortoast(languageData!!.username_can_not_be_blank)
            return false
        }

        if (!Utility.checkMinimumAndMaximumUsernameCharacter(
                binding!!.susername.text.toString().trim()
            )
        ) {
            Errortoast(languageData!!.username_should_be_between_4_to_12_characters)
            return false
        }

        if (binding!!.sdateOfBirth.text.toString().trim().isEmpty()) {
            Errortoast(languageData!!.date_of_birth_can_not_be_blank)
            return false
        }
        return true
    }

    private fun setupLanguage() {
        binding!!.semail.hint = languageData!!.pemail
        binding!!.spassword.hint = languageData!!.ppassword
        binding!!.sconfirmpassword.hint = languageData!!.pconfirmpassword
        binding!!.susername.hint = languageData!!.pusername
        binding!!.sdateOfBirth.hint = languageData!!.pdateofbirth
        binding!!.btnNextStepOne.text = languageData!!.pnext
        binding!!.btnCancelStepOne.text = languageData!!.pcancel
        binding!!.txtStepOne.text = languageData!!.pstepone
    }

    override fun onSignupSuccess(data: UserData.Data) {
        checkIfFirebaseUserExist(data)
    }

    private fun checkIfFirebaseUserExist(responseData: UserData.Data) {

        val email = responseData.userid.toString().toLowerCase() + "_1" + "@amiggos.com"
        val password = "amiggos@123"

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createOrUpdateUserFirebaseUser(
                    responseData,
                    sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN)!!
                )
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        createOrUpdateUserFirebaseUser(
                            responseData,
                            sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN)!!
                        )
                    } else {
                        Toast.makeText(
                            applicationContext,
                            task.exception!!.message!!,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }


    private fun createOrUpdateUserFirebaseUser(
        data: UserData.Data,
        fcmToken: String
    ) {
        val firebaseUser = auth.currentUser
        val user = User()
        user.amiggosID = data.unique_timestamp.toString()
        user.deviceToken = data.apiToken
        user.email = data.email
        user.fcmToken = fcmToken
        user.image = data.profile
        user.name = ""
        user.timestamp = System.currentTimeMillis()
        database.child(ConstantLib.USER).child(firebaseUser!!.uid).setValue(user)

        saveUserInfo(data)


//        val stepOneModel = StepOneModel(binding!!.semail.text.toString().trim(),binding!!.spassword.text.toString().trim(),binding!!.susername.text.toString().trim(),dateOfBirth)
//        val intent = Intent(applicationContext,StepTwo::class.java)
//        intent.putExtra("steponedata",stepOneModel)
//        startActivity(intent)
//        Animatoo.animateSlideRight(this)

        startActivity(Intent(applicationContext, AHomeScreen::class.java))
        finishAffinity()
        Animatoo.animateSlideRight(this)


    }

//    {
//        "status": true,
//        "message": "User registered successfully.",
//        "data": {
//        "email": "trilokwarke@gmail.com",
//        "username": "trilokwarke",
//        "dob": "10\/5\/1994",
//        "device_type": "2",
//        "device_id": "dHnwnokSQKqizvcLO5dTub:APA91bGMn4efHqzyI5kv3E6sz0bKOdyqkiI0Bih_jZOrreS_cP24VkcTM5sOc8KppZSl4RNA_BgXnxzhglb7dH4T2GaMys5PtWhqGcccuRn5rio0LHi5Rm25wGy37Ij6WPBs8l6c2TWN",
//        "api_token": "QIbGlKBOmCCs6ZybJ3KmWrzH5AkfRQZRXH0Nn9oV",
//        "status": 1,
//        "is_active": 0,
//        "is_profile_complete": 0,
//        "userid": 43,
//        "type": "4",
//        "profile": "http:\/\/tekdev.tekzee.in\/Amiggos\/public\/uploads\/default.png"
//    }
//    }

    private fun saveUserInfo(data: UserData.Data) {
        sharedPreferences!!.save(ConstantLib.USER_ID, data.userid.toInt())
        sharedPreferences!!.save(ConstantLib.UNIQUE_TIMESTAMP, data.unique_timestamp.toString())
        sharedPreferences!!.save(ConstantLib.USER_NAME, data.username)
        sharedPreferences!!.save(ConstantLib.USER_EMAIL, data.email)
        sharedPreferences!!.save(ConstantLib.USER_DOB, data.dob)
        sharedPreferences!!.save(ConstantLib.API_TOKEN, data.apiToken)
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, data.profile)
        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        sharedPreferences!!.save(ConstantLib.USER_AGE, data.age)
        sharedPreferences!!.save(ConstantLib.USER_TYPE, data.type)
        sharedPreferences!!.save(ConstantLib.MYID, data.myid)
    }


    override fun onSignUpFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = monthOfYear + 1
        binding!!.sdateOfBirth.setText("$month/$dayOfMonth/$year")
        dateOfBirth = "$year-$month-$dayOfMonth"
    }
}
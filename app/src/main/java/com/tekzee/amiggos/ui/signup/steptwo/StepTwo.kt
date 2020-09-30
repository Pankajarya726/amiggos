package com.tekzee.amiggos.ui.signup.steptwo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ajithvgiri.searchdialog.OnSearchItemSelected
import com.ajithvgiri.searchdialog.SearchListItem
import com.ajithvgiri.searchdialog.SearchableDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.StepTwoBinding
import com.tekzee.amiggos.ui.signup.StepOneModel
import com.tekzee.amiggos.ui.signup.login_new.ALogin
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.signup.steptwo.model.UserData

class StepTwo: BaseActivity(), StepTwoPresenter.StepTwoPresenterMainView {

    private var cityData = ArrayList<CityResponse.Data.City>()
    private var stateData = ArrayList<StateResponse.Data.States>()
    private var binding: StepTwoBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var stepTwoImplementation: StepTwoImplementation? = null
    private var cityId :String? = ""
    private var stateId :String? =""
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.step_two)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        stepTwoImplementation = StepTwoImplementation(this,this)
        database = FirebaseDatabase.getInstance().reference
        setupLanguage()
        setupClickListener()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() {
        binding!!.btnBackStepTwo.setOnClickListener {
            onBackPressed()
        }

        binding!!.btnDoneStepTwo.setOnClickListener {
            if(validateFields()){
                callSignUpActivity()
            }
        }

        binding!!.btnSkip.setOnClickListener {
            startActivity(Intent(applicationContext, AHomeScreen::class.java))
            finishAffinity()
            Animatoo.animateSlideRight(this)
        }

        binding!!.tstate.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP){
                callStateApi()
            }
            true
        }

        binding!!.tcity.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP){
                    callGetCity()
            }
            true
        }
    }

    private fun callSignUpActivity() {
        val input = JsonObject()
        val dataFromStepOne = intent.getSerializableExtra("steponedata") as StepOneModel
        input.addProperty("userid",sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("first_name",binding!!.tfirstname.text.toString().trim())
        input.addProperty("last_name",binding!!.tlastname.text.toString().trim())
        input.addProperty("date_of_birth",dataFromStepOne.dateofbirth)
        input.addProperty("state",stateId)
        input.addProperty("city",cityId)
        input.addProperty("phone_number",binding!!.tphone.text.toString().trim())

        input.addProperty("pronouns", "")
        input.addProperty("image","")
        stepTwoImplementation!!.doUpdateUserInformation(input, Utility.createHeaders(sharedPreferences))
    }

    private fun callStateApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("country_id", "231")
        stepTwoImplementation!!.doCallStateApi(input, Utility.createHeaders(sharedPreferences))
    }

    private fun validateFields(): Boolean {
        if(binding!!.tfirstname.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"First name can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.tlastname.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Last name can not be blank...",Toast.LENGTH_LONG).show()
            return false
        } else if(stateId!!.isEmpty()){
            Toast.makeText(applicationContext,"State can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }else if(cityId!!.isEmpty()){
            Toast.makeText(applicationContext,"City can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.tphone.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Phone no can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }else if(stateId!!.isEmpty()){
            Toast.makeText(applicationContext,"State can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }else if(cityId!!.isEmpty()){
            Toast.makeText(applicationContext,"City can not be blank...",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setupLanguage() {
        binding!!.tfirstname.hint = languageData!!.pfirstname
        binding!!.tlastname.hint = languageData!!.plastname
        binding!!.tcity.hint = languageData!!.pcity
        binding!!.tstate.hint = languageData!!.pstate
        binding!!.tphone.hint = languageData!!.pphone
        binding!!.btnDoneStepTwo.text = languageData!!.pdone
        binding!!.btnBackStepTwo.text = languageData!!.pback
        binding!!.txtStepTwo.text = languageData!!.psteptwo
        binding!!.btnSkip.text = languageData!!.skip
    }


    override fun onStateSuccess(responseData: List<StateResponse.Data.States>) {
        stateData.clear()
        stateData.addAll(responseData)
        createStatePopup(stateData)
    }

    override fun onSignupSuccess(data: UserData.Data) {
//        sharedPreferences!!.save(ConstantLib.USER_ID, responseData.userid.toInt())
//        sharedPreferences!!.save(ConstantLib.USER_NAME, responseData.username)
//        sharedPreferences!!.save(ConstantLib.USER_EMAIL, responseData.email)
//        sharedPreferences!!.save(ConstantLib.USER_DOB, responseData.dob)
//        sharedPreferences!!.save(ConstantLib.API_TOKEN, responseData.apiToken)
//        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, responseData.profile)
//        sharedPreferences!!.save(ConstantLib.ISAGREE, false)
        UpdateInfoInFirebase()
        startActivity(Intent(applicationContext, AHomeScreen::class.java))
        finishAffinity()
        Animatoo.animateSlideRight(this)
    }

    private fun UpdateInfoInFirebase() {
        val firebaseUser = auth.currentUser
        database.child(ConstantLib.USER).child(firebaseUser!!.uid).child("name").setValue(binding!!.tfirstname.text.toString().trim()+" "+binding!!.tlastname.text.toString().trim())

    }

    override fun onSignUpFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun createStatePopup(stateData: ArrayList<StateResponse.Data.States>) {
        val searchListItems = ArrayList<SearchListItem>()
        for(items in stateData){
            searchListItems.add(SearchListItem(items.id,items.name))
        }
        val searchableDialogState= SearchableDialog(this, searchListItems, "Select State")
        searchableDialogState.setOnItemSelected(object: OnSearchItemSelected{
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.tstate.setText(searchListItem.title)
                stateId = searchListItem.id.toString()
                cityId =""
                binding!!.tcity.setText("")
                searchableDialogState.dismiss()
            }
        }) // implement 'OnSearchItemSelected'in your Activity
        searchableDialogState.show()
    }


    override fun onCitySuccess(responseData: List<CityResponse.Data.City>) {
        cityData.clear()
        cityData.addAll(responseData)
        createCityPopup()
    }

    private fun createCityPopup() {
        val searchListItems = ArrayList<SearchListItem>()
        for(items in cityData){
            searchListItems.add(SearchListItem(items.id,items.name))
        }
        val searchableDialogCity = SearchableDialog(this, searchListItems, "Select City")
        searchableDialogCity.setOnItemSelected(object: OnSearchItemSelected{
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.tcity.setText(searchListItem.title)
                cityId = searchListItem.id.toString()
                searchableDialogCity.dismiss()
            }
        }) // implement 'OnSearchItemSelected'in your Activity
        searchableDialogCity.show()
    }

    private fun callGetCity() {
        val input: JsonObject = JsonObject()
        input.addProperty("state_id", stateId)
        stepTwoImplementation!!.doCallCityApi(input, Utility.createHeaders(sharedPreferences))
    }

    override fun onStateFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onCityFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }


}
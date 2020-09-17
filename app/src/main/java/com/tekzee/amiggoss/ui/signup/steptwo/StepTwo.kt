package com.tekzee.amiggoss.ui.signup.steptwo

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
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.StepTwoBinding
import com.tekzee.amiggoss.ui.signup.StepOneModel
import com.tekzee.amiggoss.ui.signup.login_new.ALogin
import com.tekzee.amiggoss.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.UserData
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib

class StepTwo: BaseActivity(), StepTwoPresenter.StepTwoPresenterMainView {

    private var cityData = ArrayList<CityResponse.Data.City>()
    private var stateData = ArrayList<StateResponse.Data.States>()
    private var binding: StepTwoBinding? =null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var stepTwoImplementation: StepTwoImplementation? = null
    private var cityId :String? = ""
    private var stateId :String? =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.step_two)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        stepTwoImplementation = StepTwoImplementation(this,this)
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
        input.addProperty("email",dataFromStepOne.email)
        input.addProperty("password",dataFromStepOne.password)
        input.addProperty("username",dataFromStepOne.username)
        input.addProperty("date_of_birth",dataFromStepOne.dateofbirth)
        input.addProperty("first_name",binding!!.tfirstname.text.toString().trim())
        input.addProperty("last_name",binding!!.tlastname.text.toString().trim())
        input.addProperty("state",stateId)
        input.addProperty("city",cityId)
        input.addProperty("phone_number",binding!!.tphone.text.toString().trim())
        input.addProperty("device_type", ConstantLib.DEVICETYPE)
        input.addProperty("device_id",sharedPreferences!!.getValueString(ConstantLib.FCMTOKEN))
        stepTwoImplementation!!.doCallSignupApi(input, Utility.createHeaders(sharedPreferences))
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
        binding!!.tfirstname.hint = languageData!!.PFIRSTNAME
        binding!!.tlastname.hint = languageData!!.PLASTNAME
        binding!!.tcity.hint = languageData!!.PCITY
        binding!!.tstate.hint = languageData!!.PSTATE
        binding!!.tphone.hint = languageData!!.PPHONE
        binding!!.btnDoneStepTwo.text = languageData!!.PDONE
        binding!!.btnBackStepTwo.text = languageData!!.PBACK
        binding!!.txtStepTwo.text = languageData!!.PSTEPTWO
    }


    override fun onStateSuccess(responseData: List<StateResponse.Data.States>) {
        stateData.clear()
        stateData.addAll(responseData)
        createStatePopup(stateData)
    }

    override fun onSignupSuccess(data: UserData.Data) {
        startActivity(Intent(applicationContext,ALogin::class.java))
        finishAffinity()
        Animatoo.animateSlideRight(this)
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
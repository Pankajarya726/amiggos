package com.tekzee.amiggoss.ui.viewandeditprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ajithvgiri.searchdialog.OnSearchItemSelected
import com.ajithvgiri.searchdialog.SearchListItem
import com.ajithvgiri.searchdialog.SearchableDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ViewAndEditLayoutBinding
import com.tekzee.amiggoss.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggoss.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggoss.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggoss.util.ImagePickerUtils
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class AViewAndEditProfile: BaseActivity(), AViewAndEditPresenter.AViewAndEditPresenterMainView,
    DatePickerDialog.OnDateSetListener {



    private var dateOfBirth: String? = ""
    private var cityData = ArrayList<CityResponse.Data.City>()
    private var stateData = ArrayList<StateResponse.Data.States>()
    private var binding: ViewAndEditLayoutBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aViewAndEditImplementation: AViewAndEditImplementation? = null
    private var cityId :String? = ""
    private var stateId :String? =""
    private var imagePath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.view_and_edit_layout)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aViewAndEditImplementation = AViewAndEditImplementation(this,this)

        setupView()
        callGetProfile()
        setupClickListener()
        setupLanguage()
    }

    private fun setupLanguage() {
        binding!!.lFirstname.text = languageData!!.PFIRSTNAME
        binding!!.lLastname.text = languageData!!.PLASTNAME
        binding!!.lState.text = languageData!!.PSTATE
        binding!!.lCity.text = languageData!!.PCITY
        binding!!.lPhonenumber.text = languageData!!.PPHONE
        binding!!.lDateofbirth.text = languageData!!.PDATEOFBIRTH
        if(intent.getStringExtra(ConstantLib.FROM).equals("VIEW")){
            binding!!.txtEditProfileHeading.text = languageData!!.PVIEWPROFILE
            binding!!.bottomlayout.visibility = View.GONE
        }else{
            binding!!.txtEditProfileHeading.text = languageData!!.PEDITPROFILE
            binding!!.bottomlayout.visibility = View.VISIBLE
        }


        binding!!.txtEditProfileSubheading.text = languageData!!.PTELLUSWHOYOUARE

    }

    private fun setupClickListener() {
        binding!!.eState.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP){
                callStateApi()
            }
            true
        }

        binding!!.eCity.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP){
                callGetCity()
            }
            true
        }

        binding!!.imgUser.setOnClickListener {
            pickImage()
        }

        binding!!.back.setOnClickListener {
           onBackPressed()

        }



        binding!!.btnSave.setOnClickListener {
            if(validateFields()){
                callUpdateProfile()
            }

        }


        binding!!.eDateofbirth.setOnTouchListener { v: View, event: MotionEvent ->
            if(event.action == MotionEvent.ACTION_UP){

                val todaysDate = Calendar.getInstance()
                todaysDate.add(Calendar.YEAR,-18)
                SpinnerDatePickerDialogBuilder()
                    .context(this)
                    .callback(this)
                    .showTitle(true)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showDaySpinner(true)
                    .defaultDate(todaysDate.get(Calendar.YEAR), todaysDate.get(Calendar.MONTH), todaysDate.get(Calendar.DAY_OF_MONTH))
                    .maxDate(todaysDate.get(Calendar.YEAR), todaysDate.get(Calendar.MONTH), todaysDate.get(Calendar.DAY_OF_MONTH))
                    .minDate(1900, 0, 1)
                    .build()
                    .show()

            }
            true
        }
    }

    private fun validateFields(): Boolean {
        if(binding!!.eFirstname.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"First name can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.eLastname.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Last name can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.eDateofbirth.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Date of birth can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(stateId!!.isEmpty()){
            Toast.makeText(applicationContext,"State can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(cityId!!.isEmpty()){
            Toast.makeText(applicationContext,"City can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }else if(binding!!.ePhone.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext,"Phone number can not be blank..",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun callUpdateProfile() {
        var file: File?=null
        if (imagePath != null) {
            file = File(imagePath)
        }
        var requestFile: RequestBody?=null
        var fileMultipartBody: MultipartBody.Part?=null
        if(file!=null){
            requestFile = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                file!!
            )

            fileMultipartBody =
                MultipartBody.Part.createFormData("image", file!!.name, requestFile)

        }

        val firstnameRequestBody = RequestBody.create(
            MultipartBody.FORM, binding!!.eFirstname.text.toString().trim()
        )
        val lastnameRequestBody = RequestBody.create(
            MultipartBody.FORM, binding!!.eLastname.text.toString().trim()
        )

        val dobRequestBody = RequestBody.create(
            MultipartBody.FORM, dateOfBirth!!
        )
        val cityIdRequestBody = RequestBody.create(
            MultipartBody.FORM, cityId!!
        )
        val stateIdRequestBody = RequestBody.create(
            MultipartBody.FORM, stateId!!
        )

        val phonenumberRequestBody = RequestBody.create(
            MultipartBody.FORM, binding!!.ePhone.text.toString().trim()
        )

        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            ""+sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
        )



        if (fileMultipartBody != null) {
            aViewAndEditImplementation!!.doCallUpdateProfileApi(fileMultipartBody, firstnameRequestBody,lastnameRequestBody,dobRequestBody,
                cityIdRequestBody,stateIdRequestBody,phonenumberRequestBody,useridRequestBody,Utility.createHeaders(sharedPreferences))
        }else{
            aViewAndEditImplementation!!.doCallUpdateProfileApi(null, firstnameRequestBody,lastnameRequestBody,dobRequestBody,
                cityIdRequestBody,stateIdRequestBody,phonenumberRequestBody,useridRequestBody,Utility.createHeaders(sharedPreferences))
        }

    }

    private fun callGetCity() {
        val input: JsonObject = JsonObject()
        input.addProperty("state_id", stateId)
        aViewAndEditImplementation!!.doCallCityApi(input, Utility.createHeaders(sharedPreferences))
    }


    private fun callStateApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("country_id", "231")
        aViewAndEditImplementation!!.doCallStateApi(input, Utility.createHeaders(sharedPreferences))
    }


    private fun callGetProfile() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        aViewAndEditImplementation!!.callGetProfile(input, Utility.createHeaders(sharedPreferences))
    }

    private fun setupView() {
        if(intent.getStringExtra(ConstantLib.FROM).equals("EDIT",true)){
            setupEditProfile()
        }else{
            setupViewProfile()
        }
    }

    private fun setupViewProfile() {

        binding!!.eFirstname.isEnabled = false
        binding!!.eLastname.isEnabled = false
        binding!!.eCity.isEnabled = false
        binding!!.eState.isEnabled = false
        binding!!.ePhone.isEnabled = false
        binding!!.eDateofbirth.isEnabled = false
        binding!!.imgUser.isEnabled = false

    }

    private fun setupEditProfile() {

        binding!!.eFirstname.isEnabled = true
        binding!!.eLastname.isEnabled = true
        binding!!.eCity.isEnabled = true
        binding!!.eState.isEnabled = true
        binding!!.ePhone.isEnabled = true
        binding!!.eDateofbirth.isEnabled = true
    }




    override fun onGetProfileSuccess(responseData: GetUserProfileResponse?) {
        setupProfileData(responseData!!.data)
    }

    override fun onStateSuccess(responseData: List<StateResponse.Data.States>) {
        stateData.clear()
        stateData.addAll(responseData)
        createStatePopup(stateData)
    }


    private fun createStatePopup(stateData: ArrayList<StateResponse.Data.States>) {
        val searchListItems = ArrayList<SearchListItem>()
        for(items in stateData){
            searchListItems.add(SearchListItem(items.id,items.name))
        }
        val searchableDialogState= SearchableDialog(this, searchListItems, "Select State")
        searchableDialogState.setOnItemSelected(object: OnSearchItemSelected {
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.eState.setText(searchListItem.title)
                stateId = searchListItem.id.toString()
                cityId =""
                binding!!.eCity.setText("")
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

    override fun onProfileUpdateSuccess(data: UpdateProfileResponse?) {
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE,data!!.data.user_image)
        Toast.makeText(applicationContext,data.message,Toast.LENGTH_LONG).show()
    }

    override fun onProfileUpdateFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun createCityPopup() {
        val searchListItems = ArrayList<SearchListItem>()
        for(items in cityData){
            searchListItems.add(SearchListItem(items.id,items.name))
        }
        val searchableDialogCity = SearchableDialog(this, searchListItems, "Select City")
        searchableDialogCity.setOnItemSelected(object: OnSearchItemSelected{
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.eCity.setText(searchListItem.title)
                cityId = searchListItem.id.toString()
                searchableDialogCity.dismiss()
            }
        }) // implement 'OnSearchItemSelected'in your Activity
        searchableDialogCity.show()
    }

    override fun onStateFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onCityFailure(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun setupProfileData(data: GetUserProfileResponse.Data) {
        dateOfBirth = data.dob.split("-")[2]+"-"+data.dob.split("-")[1]+"-"+data.dob.split("-")[0]
        binding!!.eFirstname.setText(data.name)
        binding!!.eLastname.setText(data.lastName)
        binding!!.eCity.setText(data.city)
        binding!!.eState.setText(data.state)
        binding!!.ePhone.setText(data.phone)
//        16-05-1988
        binding!!.eDateofbirth.setText(data.dob)
        Glide.with(applicationContext).load(data.profile).placeholder(R.drawable.user).into(binding!!.imgUser)
        cityId = data.city_id
        stateId = data.state_id

    }

    override fun onStop() {
        super.onStop()
        aViewAndEditImplementation!!.onStop()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun pickImage() {
        CropImage.activity()
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result =
                CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                imagePath = ImagePickerUtils.getPath(this, result.uri)
                binding!!.imgUser.setImageURI(Uri.parse(imagePath))
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(
                    this,
                    "Cropping failed: " + result.error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val month = monthOfYear + 1
        binding!!.eDateofbirth.setText("$dayOfMonth-$month-$year")
        dateOfBirth = "$year-$month-$dayOfMonth"

    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }
}
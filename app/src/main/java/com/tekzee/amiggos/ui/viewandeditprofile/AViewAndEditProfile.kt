package com.tekzee.amiggos.ui.viewandeditprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.ajithvgiri.searchdialog.OnSearchItemSelected
import com.ajithvgiri.searchdialog.SearchListItem
import com.ajithvgiri.searchdialog.SearchableDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.impulsiveweb.galleryview.GalleryView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ViewAndEditLayoutBinding
import com.tekzee.amiggos.ui.signup.steptwo.model.CityResponse
import com.tekzee.amiggos.ui.signup.steptwo.model.StateResponse
import com.tekzee.amiggos.ui.viewandeditprofile.adapter.PhotoAdapter
import com.tekzee.amiggos.ui.viewandeditprofile.model.GetUserProfileResponse
import com.tekzee.amiggos.ui.viewandeditprofile.model.UpdateProfileResponse
import com.tekzee.amiggos.util.GetFilePath
import com.tekzee.amiggos.util.ImagePickerUtils
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.tsongkha.spinnerdatepicker.DatePicker
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AViewAndEditProfile : BaseActivity(), AViewAndEditPresenter.AViewAndEditPresenterMainView,
    DatePickerDialog.OnDateSetListener, PhotoDeleteClickListener {


    private val FILE_REQUEST_CODE: Int = 500
    private lateinit var profile_name: String
    private lateinit var adapter: PhotoAdapter
    private var dateOfBirth: String? = ""
    private var cityData = ArrayList<CityResponse.Data.City>()
    private var stateData = ArrayList<StateResponse.Data.States>()
    private var binding: ViewAndEditLayoutBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aViewAndEditImplementation: AViewAndEditImplementation? = null
    private var cityId: String? = ""
    private var stateId: String? = ""
    private var imagePath: String? = null
    private var pickImageFor = 0 //1-profile 2-uploadphoto
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.view_and_edit_layout)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aViewAndEditImplementation = AViewAndEditImplementation(this, this)

        setupView()
        callGetProfile()
        setupClickListener()
        setupLanguage()
        setupAdapter(arrayListOf())
    }

    private fun setupLanguage() {
        binding!!.lFirstname.text = languageData!!.pfirstname
        binding!!.lLastname.text = languageData!!.plastname
        binding!!.lState.text = languageData!!.pstate
        binding!!.lCity.text = languageData!!.pcity
        binding!!.lPhonenumber.text = languageData!!.pphone
        binding!!.lDateofbirth.text = languageData!!.pdateofbirth
        binding!!.addPhotos.text = languageData!!.addphotos
        binding!!.txtEditProfileHeading.text = intent.getStringExtra(ConstantLib.NAME)
        binding!!.btnSave.text = languageData!!.klSAVE
        binding!!.txtEditProfileSubheading.text = languageData!!.peditprofile

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupClickListener() {
        binding!!.eState.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                callStateApi()
            }
            true
        }

        binding!!.eCity.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                callGetCity()
            }
            true
        }

        binding!!.imgUser.setOnClickListener {
            pickImageFor = 1
            pickImage()
        }

        binding!!.txtEditPhoto.setOnClickListener {
            pickImageFor = 1
            pickImage()
        }

        binding!!.back.setOnClickListener {
            onBackPressed()

        }



        binding!!.btnSave.setOnClickListener {
            if (validateFields()) {
                callUpdateProfile()
            }

        }


        binding!!.eDateofbirth.setOnTouchListener { v: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP) {

                val todaysDate = Calendar.getInstance()
                todaysDate.add(Calendar.YEAR, -18)
                SpinnerDatePickerDialogBuilder()
                    .context(this)
                    .callback(this)
                    .showTitle(true)
                    .spinnerTheme(R.style.NumberPickerStyle)
                    .showDaySpinner(true)
                    .defaultDate(
                        todaysDate.get(Calendar.YEAR),
                        todaysDate.get(Calendar.MONTH),
                        todaysDate.get(
                            Calendar.DAY_OF_MONTH
                        )
                    )
                    .maxDate(
                        todaysDate.get(Calendar.YEAR),
                        todaysDate.get(Calendar.MONTH),
                        todaysDate.get(
                            Calendar.DAY_OF_MONTH
                        )
                    )
                    .minDate(1900, 0, 1)
                    .build()
                    .show()

            }
            true
        }

        binding!!.addPhotos.setOnClickListener {
//
//            PickPhotoCustomDialog.Builder(supportFragmentManager)
//                .setUploadExistingPhotoText(languageData!!.uploadexistingphoto)
//                .setTakeNewPhotoText(languageData!!.takenewphoto)
//                .setCancelText(languageData!!.klCancel)
//                .setOnCloseButton {
//                  Log.e("closed", "closed")
//                }.setOnTakeNewPhoto {
//                    pickImageFor = 2
//                    pickImage()
//                }
//                .setOnUploadExistingPhoto{
//                    pickImageFor = 2
//                    pickImage()
//                }
//                .build()
//                .show()


            val dialog: com.tekzee.amiggos.custom.BottomDialog =
                com.tekzee.amiggos.custom.BottomDialog.newInstance(
                    "",
                    arrayOf(languageData!!.uploadexistingphoto, languageData!!.takenewphoto)
                )
            dialog.show(supportFragmentManager, "dialog")
            dialog.setListener { position ->
                if (position == 0) {
                    pickImageFor = 2
                    FilePickerBuilder.instance
                        .setMaxCount(5) //optional
//                        .setSelectedFiles(filePaths) //optional
                        .setActivityTheme(R.style.LibAppTheme) //optional
                        .pickPhoto(this)
                } else {
                    pickImageFor = 2
                    pickImage()
                }

            }
        }
    }

    private fun validateFields(): Boolean {
        if (binding!!.eFirstname.text.toString().trim().isEmpty()) {
            Toast.makeText(applicationContext, "First name can not be blank..", Toast.LENGTH_LONG)
                .show()
            return false
        } else if (binding!!.eLastname.text.toString().trim().isEmpty()) {
            Toast.makeText(applicationContext, "Last name can not be blank..", Toast.LENGTH_LONG)
                .show()
            return false
        } else if (binding!!.eDateofbirth.text.toString().trim().isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Date of birth can not be blank..",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (stateId!!.isEmpty()) {
            Toast.makeText(applicationContext, "State can not be blank..", Toast.LENGTH_LONG).show()
            return false
        } else if (cityId!!.isEmpty()) {
            Toast.makeText(applicationContext, "City can not be blank..", Toast.LENGTH_LONG).show()
            return false
        } else if (binding!!.ePhone.text.toString().trim().isEmpty()) {
            Toast.makeText(applicationContext, "Phone number can not be blank..", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    private fun callUpdateProfile() {
        var file: File? = null
        if (imagePath != null) {
            file = File(imagePath)
        }
        var requestFile: RequestBody? = null
        var fileMultipartBody: MultipartBody.Part? = null
        if (file != null) {
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
            "" + sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
        )



        if (fileMultipartBody != null) {
            aViewAndEditImplementation!!.doCallUpdateProfileApi(
                fileMultipartBody,
                firstnameRequestBody,
                lastnameRequestBody,
                dobRequestBody,
                cityIdRequestBody,
                stateIdRequestBody,
                phonenumberRequestBody,
                useridRequestBody,
                Utility.createHeaders(
                    sharedPreferences
                )
            )
        } else {
            aViewAndEditImplementation!!.doCallUpdateProfileApi(
                null,
                firstnameRequestBody,
                lastnameRequestBody,
                dobRequestBody,
                cityIdRequestBody,
                stateIdRequestBody,
                phonenumberRequestBody,
                useridRequestBody,
                Utility.createHeaders(
                    sharedPreferences
                )
            )
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
        if (intent.getStringExtra(ConstantLib.FROM).equals("EDIT", true)) {
            setupEditProfile()
        } else {
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
        setupImageArray(responseData!!.data.otherImages)
    }

    private fun setupImageArray(otherImages: List<GetUserProfileResponse.Data.OtherImage>) {
        val newlist = ArrayList<GetUserProfileResponse.Data.OtherImage>()
        newlist.addAll(otherImages)
        adapter.submitList(newlist)
    }

    private fun setupAdapter(response: List<GetUserProfileResponse.Data.OtherImage>) {
        adapter = PhotoAdapter(this, response)
        binding!!.recyclerPhotos.layoutManager = GridLayoutManager(
            this,
            2
        )
        binding!!.recyclerPhotos.adapter = adapter
    }


    override fun onStateSuccess(responseData: List<StateResponse.Data.States>) {
        stateData.clear()
        stateData.addAll(responseData)
        createStatePopup(stateData)
    }


    private fun createStatePopup(stateData: ArrayList<StateResponse.Data.States>) {
        val searchListItems = ArrayList<SearchListItem>()
        for (items in stateData) {
            searchListItems.add(SearchListItem(items.id, items.name))
        }
        val searchableDialogState = SearchableDialog(this, searchListItems, "Select State")
        searchableDialogState.setOnItemSelected(object : OnSearchItemSelected {
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.eState.setText(searchListItem.title)
                stateId = searchListItem.id.toString()
                cityId = ""
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


//        {
//            "status": true,
//            "message": "",
//            "data": {
//            "userid": 43,
//            "username": "trilokwarke",
//            "email": "trilokwarke@gmail.com",
//            "name": "Trilok",
//            "last_name": "Warke",
//            "dob": "05\/10\/1996",
//            "city_id": "42672",
//            "state_id": "3920",
//            "phone": "8602119024",
//            "api_token": "QIbGlKBOmCCs6ZybJ3KmWrzH5AkfRQZRXH0Nn9oV",
//            "device_type": "2",
//            "firebase_id": null,
//            "pronouns": "",
//            "profile": "http:\/\/tekdev.tekzee.in\/Amiggos\/public\/uploads\/user\/customer\/default.png",
//            "age": 24,
//            "state": "Alaska",
//            "city": "Bethel",
//            "is_profile_complete": 1,
//            "other_images": [],
//            "visible_map": 1,
//            "message_receive": 1,
//            "push_notification": 1,
//            "profile_name": "",
//            "address": "Alaska,Bethel",
//            "type": "4"
//        }
//        }

        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, data!!.data.user_image)
        Toast.makeText(applicationContext, data.message, Toast.LENGTH_LONG).show()
        callGetProfile()
    }

    override fun onUploadImageSuccess(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        callGetProfile()
    }

    override fun onUpdateProfileImage(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        callGetProfile()
    }

    override fun onProfileUpdateFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun createCityPopup() {
        val searchListItems = ArrayList<SearchListItem>()
        for (items in cityData) {
            searchListItems.add(SearchListItem(items.id, items.name))
        }
        val searchableDialogCity = SearchableDialog(this, searchListItems, "Select City")
        searchableDialogCity.setOnItemSelected(object : OnSearchItemSelected {
            override fun onClick(position: Int, searchListItem: SearchListItem) {
                binding!!.eCity.setText(searchListItem.title)
                cityId = searchListItem.id.toString()
                searchableDialogCity.dismiss()
            }
        }) // implement 'OnSearchItemSelected'in your Activity
        searchableDialogCity.show()
    }

    override fun onStateFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onCityFailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onPhotoDeleted(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        callGetProfile()
    }

    private fun setupProfileData(data: GetUserProfileResponse.Data) {
        sharedPreferences!!.save(ConstantLib.ISPROFILECOMPLETE, data.isProfileComplete)
        dateOfBirth = data.dob
        binding!!.txtEditProfileHeading.text = data.name + " " + data.lastName
        binding!!.eFirstname.setText(data.name)
        binding!!.eLastname.setText(data.lastName)
        binding!!.eCity.setText(data.city)
        binding!!.eState.setText(data.state)
        binding!!.ePhone.setText(data.phone)
        binding!!.eDateofbirth.setText(data.dob)
        Glide.with(applicationContext).load(data.profile).placeholder(R.drawable.noimage)
            .into(binding!!.imgUser)
        cityId = data.cityId
        stateId = data.stateId
        profile_name = data.profile

        saveDataToSharedPreference(data)

    }

    private fun saveDataToSharedPreference(data: GetUserProfileResponse.Data) {
        sharedPreferences!!.save(ConstantLib.USER_NAME, data.name + " " + data.lastName)
        sharedPreferences!!.save(ConstantLib.USER_DOB, data.name + " " + data.dob)
        sharedPreferences!!.save(ConstantLib.PROFILE_IMAGE, data.profile)
        sharedPreferences!!.save(ConstantLib.USER_AGE, data.age)
        UpdateInfoInFirebase(data)
    }


    private fun UpdateInfoInFirebase(data: GetUserProfileResponse.Data) {
        val firebaseUser = auth.currentUser
        database.child(ConstantLib.USER).child(firebaseUser!!.uid).child("name")
            .setValue(data.name + " " + data.lastName)
        database.child(ConstantLib.USER).child(firebaseUser.uid).child("image")
            .setValue(data.profile)

    }

    override fun onStop() {
        super.onStop()
        aViewAndEditImplementation!!.onStop()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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

        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            val photoPaths = ArrayList<Uri>()
            photoPaths.addAll(data!!.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_MEDIA));
            uploadMultipleUserImage(photoPaths)
        } else
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result =
                    CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    imagePath = ImagePickerUtils.getPath(this, result.uri)
                    if (pickImageFor == 1) {
                        binding!!.imgUser.setImageURI(Uri.parse(imagePath))
                    } else {
                        uploadUserImage()
                    }


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(
                        this,
                        "Cropping failed: " + result.error,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun uploadUserImage() {
        var file: File? = null
        if (imagePath != null) {
            file = File(imagePath)
        }
        var requestFile: RequestBody? = null
        var fileMultipartBody: MultipartBody.Part? = null
        if (file != null) {
            requestFile = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                file
            )
            val imagearray = ArrayList<String>()
            imagearray.add(file.name)
            fileMultipartBody =
                MultipartBody.Part.createFormData("image[]", imagearray.toString(), requestFile)

        }

        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            "" + sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
        )



        if (fileMultipartBody != null) {
            aViewAndEditImplementation!!.doUpdateUserSingleImageApi(
                fileMultipartBody,
                useridRequestBody,
                Utility.createHeaders(
                    sharedPreferences
                )
            )
        } else {
            Toast.makeText(applicationContext, "Image can not be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadMultipleUserImage(photoPaths: ArrayList<Uri>) {


        val surveyImagesParts = arrayOfNulls<MultipartBody.Part>(
            photoPaths.size
        )

        for (x in 0 until photoPaths.size) {
            val file: File = File(
                GetFilePath.getFilePath(application, photoPaths[x])
            )
            val imagearray = RequestBody.create(
                "image/*".toMediaTypeOrNull(),
                file
            )
            surveyImagesParts[x] = MultipartBody.Part.createFormData(
                "image[]",
                file.name+System.currentTimeMillis(),
                imagearray
            )
        }



        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            "" + sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
        )

        aViewAndEditImplementation!!.doUpdateUserImageApi(
            surveyImagesParts,
            useridRequestBody,
            Utility.createHeaders(
                sharedPreferences
            )
        )
    }
    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val month = monthOfYear + 1
        binding!!.eDateofbirth.setText("$month/$dayOfMonth/$year")
        dateOfBirth = "$month/$dayOfMonth/$year"

    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }

    override fun OnPhotoDeleteClicked(position: Int, photoid: String) {
        val dialog: com.tekzee.amiggos.custom.BottomDialog =
            com.tekzee.amiggos.custom.BottomDialog.newInstance(
                "",
                arrayOf(languageData!!.klDeleteTitle1)
            )
        dialog.show(supportFragmentManager, "dialog")
        dialog.setListener { position ->
            deletePhoto(photoid)
        }

    }

    override fun onUpdateProfile(listItem: GetUserProfileResponse.Data.OtherImage) {
        val dialog: com.tekzee.amiggos.custom.BottomDialog =
            com.tekzee.amiggos.custom.BottomDialog.newInstance(
                "",
                arrayOf(languageData!!.viewimage, languageData!!.setprofilepicture)
            )
        dialog.show(supportFragmentManager, "dialog")
        dialog.setListener { position ->
            if (position == 0) {
                val paths: ArrayList<String> = ArrayList()
                paths.add(listItem.image)
                GalleryView.show(this, paths)
            } else if (position == 1) {
                if (profile_name.equals(listItem.id, true)) {
                    Toast.makeText(
                        applicationContext,
                        languageData!!.pleaseselectdifferentimage,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    setDefaultProfile(listItem.id)
                }

            }
        }
    }

    private fun setDefaultProfile(id: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("image_name", id)
        aViewAndEditImplementation!!.updateProfileImage(
            input, Utility.createHeaders(
                sharedPreferences
            )
        )
    }

    private fun deletePhoto(photoid: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("image", photoid)
        aViewAndEditImplementation!!.deletePhoto(input, Utility.createHeaders(sharedPreferences))
    }
}
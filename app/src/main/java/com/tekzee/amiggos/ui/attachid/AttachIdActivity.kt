package com.tekzee.amiggos.ui.attachid

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.AttachidActivityBinding
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.statusview.StatusViewActivity
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class AttachIdActivity : BaseActivity(), AttachIdActivityPresenter.AttachIdMainView,
    DatePickerDialog.OnDateSetListener {



    lateinit var binding: AttachidActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var attachIdPresenterImplementation: AttachIdPresenterImplementation? = null
    private var file: File? = null
    private var dob: String = ""
    private var userid: String ="";


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.attachid_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        attachIdPresenterImplementation = AttachIdPresenterImplementation(this, this)

            if(intent.getStringExtra(ConstantLib.FROM) == ConstantLib.NOTIFICAION){
                userid = intent.getStringExtra(ConstantLib.USER_DATA)
            }else{
                userid = sharedPreferences!!.getValueInt(ConstantLib.USER_ID).toString()
            }



        setupToolBar()
        setupClickListener()
        initViewData()
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        dob = "$dayOfMonth/${monthOfYear + 1}/$year"
        binding.txtSelectDate.text = dob
    }

    private fun initViewData() {

        binding.txtHeading.text = languageData!!.klTakePicOfID.split("\\n")[0]
        binding.txtSubtitle.text = languageData!!.klTakePicOfID.split("\\n")[1]
        binding.txtSelectDate.text = languageData!!.klSelectYourBirthDate
        binding.btnSave.text = languageData!!.klSAVE


    }

    private fun setupClickListener() {
        binding.imgUser.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        binding.txtSelectDate.setOnClickListener {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)
            val dpd = DatePickerDialog.newInstance(
                this,
                calendar.get(Calendar.YEAR), // Initial year selection
                calendar.get(Calendar.MONTH), // Initial month selection
                calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
            )
            dpd.maxDate = calendar
            dpd.show(supportFragmentManager,"Datepickerdialog")
        }

        binding.btnSave.setOnClickListener {
            if (file == null) {
                Toast.makeText(
                    applicationContext,
                    "Please upload ID",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val requestFile = RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    file!!
                )
                val fileMultipartBody =
                    MultipartBody.Part.createFormData("image", file!!.name, requestFile)
                val useridRequestBody = RequestBody.create(
                    MultipartBody.FORM,
                   userid
                )
                val dobRequestBody = RequestBody.create(
                    MultipartBody.FORM, dob
                )
                attachIdPresenterImplementation!!.doCallAttachIdApi(
                    fileMultipartBody,
                    useridRequestBody,
                    dobRequestBody,
                    Utility.createHeaders(sharedPreferences)
                )
            }

        }
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klAttachId
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    @SuppressLint("MissingSuperCall")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                binding.imgUser.setImageURI(resultUri)
                this.file = File(resultUri.path)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun onAttachIdSuccess(responseData: AttachIdResponse?) {
        val intent = Intent(this, StatusViewActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

}
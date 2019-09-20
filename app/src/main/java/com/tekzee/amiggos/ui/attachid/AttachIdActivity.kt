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
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class AttachIdActivity:BaseActivity(), AttachIdActivityPresenter.AttachIdMainView {


    lateinit var binding: AttachidActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var attachIdPresenterImplementation: AttachIdPresenterImplementation? = null
    private var file: File? = null
    private var dob: String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.attachid_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        attachIdPresenterImplementation = AttachIdPresenterImplementation(this,this)
        setupToolBar()
        setupClickListener()
        initViewData()
    }

    private fun initViewData() {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR,-18)
        binding.calendarView.setDate(calendar.timeInMillis,true,true)
        binding.calendarView.maxDate = calendar.timeInMillis

        binding.txtHeading.text = languageData!!.klTakePicOfID.split("\\n")[0]
        binding.txtSubtitle.text = languageData!!.klTakePicOfID.split("\\n")[1]
        binding.txtSelectDate.text = languageData!!.klSelectYourBirthDate
        binding.btnSave.text = languageData!!.klSAVE

        dob =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/${calendar.get(
                Calendar.YEAR
            )}"
        binding.calendarView.setOnDateChangeListener { _, year, month, date ->
            dob = "$date/${month+1}/$year"
        }
    }

    private fun setupClickListener() {
        binding.imgUser.setOnClickListener{
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)
        }

        binding.btnSave.setOnClickListener{
            if(file == null){
                Toast.makeText(applicationContext,"Please select profile picture",Toast.LENGTH_LONG).show()
            }else{
                val requestFile = RequestBody.create(
                    MediaType.parse("image/*"),
                    file
                )
                val fileMultipartBody = MultipartBody.Part.createFormData("image", file!!.name, requestFile)
                val useridRequestBody = RequestBody.create(
                    MultipartBody.FORM, sharedPreferences!!.getValueInt(ConstantLib.USER_ID).toString()
                )
                val dobRequestBody = RequestBody.create(
                    MultipartBody.FORM, dob
                )
                attachIdPresenterImplementation!!.doCallAttachIdApi(fileMultipartBody,useridRequestBody,dobRequestBody,Utility.createHeaders(sharedPreferences))
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
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
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
        val intent = Intent(this,StatusViewActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

}
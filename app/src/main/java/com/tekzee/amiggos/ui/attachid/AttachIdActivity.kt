package com.tekzee.amiggos.ui.attachid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.AttachidActivityBinding
import com.tekzee.amiggos.ui.attachid.model.AttachIdResponse
import com.tekzee.amiggos.ui.attachid.model.MyIdResponse
import com.tekzee.amiggos.util.ImagePickerUtils
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class AttachIdActivity : BaseActivity(), AttachIdActivityPresenter.AttachIdMainView {


    lateinit var binding: AttachidActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var attachIdPresenterImplementation: AttachIdPresenterImplementation? = null
    private var file: File? = null
    private var dob: String = ""
    private var userid: String = "";
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.attachid_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        attachIdPresenterImplementation = AttachIdPresenterImplementation(this, this)

        setupClickListener()
        initViewData()
        getMyId()
    }

    private fun getMyId() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        attachIdPresenterImplementation!!.getMyId(input, Utility.createHeaders(sharedPreferences))
    }


    private fun initViewData() {
        binding.title.text = languageData!!.pmyid
        binding.txtUpload.text = languageData!!.klUploadID
        binding.edit.text = languageData!!.edit
        binding.delete.text = languageData!!.delete
    }

    private fun setupClickListener() {
        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.txtUpload.setOnClickListener {
            pickImage()
        }

        binding.edit.setOnClickListener {
            pickImage()
        }

        binding.delete.setOnClickListener {
            uploadUserImage("2")
        }
    }


    private fun pickImage() {
        ImagePicker.with(this)
            .crop(16f, 9f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttachIdSuccess(responseData: AttachIdResponse?, flag_save_or_delet: String) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        getMyId()

    }

    override fun onMyIdSucess(responseData: MyIdResponse?) {
        if(responseData!!.data.imageAvailable){
            binding.myidlayout.visibility = View.VISIBLE
            binding.txtUpload.visibility = View.GONE
            sharedPreferences!!.save(ConstantLib.MYID, true)
        }else{
            binding.myidlayout.visibility = View.GONE
            binding.txtUpload.visibility = View.VISIBLE
            sharedPreferences!!.save(ConstantLib.MYID, false)
        }
        Glide.with(this).load(responseData.data.photoId).placeholder(R.drawable.noimage).into(binding.myidPicture)
    }


    override fun onAttachIdFailure(message: String, flag_save_or_delet: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        sharedPreferences!!.save(ConstantLib.MYID, false)
        getMyId()

    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imagePath = ImagePickerUtils.getPath(this, fileUri)
            uploadUserImage("1")
        }
    }

    //    1=save, 2= delete
    private fun uploadUserImage(flag_save_or_delet: String) {
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
                MultipartBody.Part.createFormData("image", imagearray.toString(), requestFile)

        }

        val useridRequestBody = RequestBody.create(
            MultipartBody.FORM,
            "" + sharedPreferences!!.getValueInt(ConstantLib.USER_ID)
        )

        val actiontypeRequestBody = RequestBody.create(
            MultipartBody.FORM,
            "" + flag_save_or_delet
        )

        if (flag_save_or_delet == "2") {
            attachIdPresenterImplementation!!.doCallAttachIdApi(
                null,
                useridRequestBody,
                actiontypeRequestBody,
                flag_save_or_delet,
                Utility.createHeaders(
                    sharedPreferences
                )
            )

        } else if (fileMultipartBody != null) {
            attachIdPresenterImplementation!!.doCallAttachIdApi(
                fileMultipartBody,
                useridRequestBody,
                actiontypeRequestBody,
                flag_save_or_delet,
                Utility.createHeaders(
                    sharedPreferences
                )
            )
        } else {
            Toast.makeText(applicationContext, "Image can not be blank", Toast.LENGTH_LONG).show()
        }


    }


}
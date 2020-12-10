package com.tekzee.amiggos.ui.cameranew

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.PictureResult
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ActivityPicturePreviewBinding
import com.tekzee.amiggos.ui.ourmemories.InviteFriendAfterCreateMemory
import com.tekzee.amiggos.ui.postmemories.PostMemories
import com.tekzee.amiggos.util.AppExecutor
import com.tekzee.amiggos.util.BitmapUtils
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.util.Utility


class PicturePreviewActivity : BaseActivity() {

    private var imageUri: Uri? = null
    private lateinit var mAppExcutor: AppExecutor
    private var mbitmap: Bitmap?=null
    private var binding: ActivityPicturePreviewBinding? = null
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_preview)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupClickListener()
        mAppExcutor = AppExecutor()


        val result: PictureResult? = picture
        if (result == null) {
            finish()
            return
        }


        try {
            result.toBitmap(
                1000,
                1000,
                BitmapCallback { bitmap: Bitmap? ->
                    binding!!.imgCapture.setImageBitmap(bitmap)
                    imageUri = BitmapUtils.saveImageAndReturnUri(applicationContext,bitmap)
                    mbitmap = bitmap
                })
        } catch (e: UnsupportedOperationException) {
            binding!!.imgCapture.setImageDrawable(ColorDrawable(Color.GREEN))
            Toast.makeText(
                this,
                "Can't preview this format: " + picture!!.format,
                Toast.LENGTH_LONG
            ).show()
        }

    }


    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.imgDownload.setOnClickListener {
            saveImageToDisk()
        }


        binding!!.imgGo.setOnClickListener {

            if (intent.getStringExtra(ConstantLib.FROM_ACTIVITY).equals(
                    "STORIEVIEWACTIVITY",
                    true
                )
            ) {

               // BitmapUtils.deleteImageFile(applicationContext,imageUri)
                val intentActivity = Intent(applicationContext, InviteFriendAfterCreateMemory::class.java)
                intentActivity.putExtra(
                    ConstantLib.OURSTORYID,
                    intent.getStringExtra(ConstantLib.OURSTORYID)
                )
                intentActivity.putExtra(ConstantLib.FILEURI, imageUri)
                intentActivity.putExtra(ConstantLib.FROM, "IMAGE")
                startActivity(intentActivity)
            } else {
//                BitmapUtils.deleteImageFile(applicationContext,imageUri)
                val intent = Intent(applicationContext, PostMemories::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.FROM, "IMAGE")
                startActivity(intent)

            }
        }
    }

    private fun saveImageToDisk() {
        mAppExcutor.diskIO().execute {
            BitmapUtils.saveImage(this, mbitmap!!)
        }
        showImageSaveDialog()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            setPictureResult(null)
        }
    }

    override fun validateError(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }

    companion object {
        private var picture: PictureResult? = null

        @JvmStatic
        fun setPictureResult(pictureResult: PictureResult?) {
            picture = pictureResult
        }
    }




    private fun showImageSaveDialog() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = "Image have been saved to gallery"
        pDialog.setCancelable(false)
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
        }
        pDialog.show()
    }


}
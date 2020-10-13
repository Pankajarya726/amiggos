package com.tekzee.amiggos.ui.postmemories

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.work.*
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.PostMemoriesBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.ourmemories.InviteFriendAfterCreateMemory
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.services.UploadWorkService


class PostMemories : BaseActivity(), PostMemoriesPresenter.PostMemoriesMainView {

    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private lateinit var binding: PostMemoriesBinding
    private var postMemoriesPresenterImplementation: PostMemoriesPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.post_memories)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        postMemoriesPresenterImplementation = PostMemoriesPresenterImplementation(this, this)
        setupToolBar()
        setupViewData()
        setupClickListerner()
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {


        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(this)
                    .setMessage("Please allow permission to upload your story")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        finish()
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

    private fun setupClickListerner() {
        binding.layoutmymemories.setOnClickListener {
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            pDialog.titleText = languageData!!.yourmemorywillbeuploadedinbackgroud
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
            }
            pDialog.setConfirmButton(languageData!!.klOk) {
                pDialog.dismiss()
                callUploadImageToMyMemories()
            }
            pDialog.show()

        }

        binding.layoutourmemories.setOnClickListener {

            if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
                val intent = Intent(applicationContext, InviteFriendAfterCreateMemory::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.OURSTORYID, "");
                intent.putExtra(ConstantLib.TAGGED_ARRAY, getIntent().getStringExtra(ConstantLib.TAGGED_ARRAY))
                intent.putExtra(ConstantLib.FROM, "VIDEO")
                startActivity(intent)
            } else {
                val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
                val intent = Intent(applicationContext, InviteFriendAfterCreateMemory::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.TAGGED_ARRAY, getIntent().getStringExtra(ConstantLib.TAGGED_ARRAY))
                intent.putExtra(ConstantLib.OURSTORYID, "");
                intent.putExtra(ConstantLib.FROM, "IMAGE")
                startActivity(intent)
            }

        }

    }

    private fun callUploadImageToMyMemories() {
        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
//            val mIntent = Intent(this, FileUploadService::class.java)
//            mIntent.putExtra(
//                ConstantLib.USER_ID,
//                sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString()
//            )
//            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
//            mIntent.putExtra(ConstantLib.FROM, "VIDEO")
//            FileUploadService.enqueueWork(this, mIntent)
//
//
//            val intent = Intent(applicationContext, AHomeScreen::class.java)
//            startActivity(intent)
//            finishAffinity()


            val data = Data.Builder().putString(ConstantLib.FILEURI, imageUri.toString())
                .putString(ConstantLib.FROM, "VIDEO")
                .putString(ConstantLib.TAGGED_ARRAY, intent.getStringExtra(ConstantLib.TAGGED_ARRAY)).build()

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();


            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(UploadWorkService::class.java).setInputData(
                    data
                ).setConstraints(constraints).addTag("Upload").build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
            val intent = Intent(applicationContext, AHomeScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = FriendsAction.SHOW_MY_MEMORY.action
            }
            startActivity(intent)
            finishAffinity()


        } else {
//            val imageUri: Uri = intent.getSerializableExtra(ConstantLib.FILEURI)!! as Uri
//            val mIntent = Intent(this, FileUploadService::class.java)
//            mIntent.putExtra(
//                ConstantLib.USER_ID,
//                sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString()
//            )
//            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
//            mIntent.putExtra(ConstantLib.FROM, "IMAGE")
//            FileUploadService.enqueueWork(this, mIntent)
//            val intent = Intent(applicationContext, AHomeScreen::class.java)
//            startActivity(intent)
//            finishAffinity()

            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
            val data = Data.Builder().putString(ConstantLib.FILEURI, imageUri.toString())
                .putString(ConstantLib.FROM, "IMAGE")
                .putString(ConstantLib.TAGGED_ARRAY, intent.getStringExtra(ConstantLib.TAGGED_ARRAY)).build()

            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();


            val oneTimeWorkRequest =
                OneTimeWorkRequest.Builder(UploadWorkService::class.java).setInputData(
                    data
                ).setConstraints(constraints).addTag("Upload").build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
            val intent = Intent(applicationContext, AHomeScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = FriendsAction.SHOW_MY_MEMORY.action
            }
            startActivity(intent)
            finishAffinity()
        }

    }

    private fun setupViewData() {
        binding.txtMymemories.text = languageData!!.klMemories
        binding.txtOurmemories.text = languageData!!.klOURMEMORY
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = ""
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

    override fun onDestroy() {
        super.onDestroy()
        postMemoriesPresenterImplementation!!.onStop()
    }
}
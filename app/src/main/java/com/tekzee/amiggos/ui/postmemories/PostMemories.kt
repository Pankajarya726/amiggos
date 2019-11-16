package com.tekzee.amiggos.ui.postmemories

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.PostMemoriesBinding
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tekzee.amiggos.ui.postmemories.service.FileUploadService
import android.content.Intent
import android.net.Uri
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.ourmemories.OurMemoriesActivity


class PostMemories : BaseActivity(), PostMemoriesPresenter.PostMemoriesMainView {

    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private lateinit var binding : PostMemoriesBinding
    private var postMemoriesPresenterImplementation: PostMemoriesPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.post_memories)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        postMemoriesPresenterImplementation = PostMemoriesPresenterImplementation(this,this)
        setupToolBar()
        setupViewData()
        setupClickListerner()
    }

    private fun setupClickListerner() {
        binding.layoutmymemories.setOnClickListener{
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            pDialog.titleText = "Your memory will be uploaded in backgroud and will take few moments to get updated"
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

        binding.layoutourmemories.setOnClickListener{

            if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
                val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
                val intent = Intent(applicationContext, OurMemoriesActivity::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.OURSTORYID,"");
                intent.putExtra(ConstantLib.FROM, "VIDEO")
                startActivity(intent)
            } else {
                val imageUri: Uri = intent.getParcelableExtra(ConstantLib.FILEURI) as Uri
                val intent = Intent(applicationContext, OurMemoriesActivity::class.java)
                intent.putExtra(ConstantLib.FILEURI, imageUri)
                intent.putExtra(ConstantLib.OURSTORYID,"");
                intent.putExtra(ConstantLib.FROM, "IMAGE")
                startActivity(intent)
            }

        }
    }

    private fun callUploadImageToMyMemories() {
        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
            val mIntent = Intent(this, FileUploadService::class.java)
            mIntent.putExtra(ConstantLib.USER_ID, sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
            mIntent.putExtra(ConstantLib.FROM, "VIDEO")
            FileUploadService.enqueueWork(this, mIntent)

            val intent = Intent(applicationContext,HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }else{
            val imageUri: Uri = intent.getParcelableExtra(ConstantLib.FILEURI) as Uri
            val mIntent = Intent(this, FileUploadService::class.java)
            mIntent.putExtra(ConstantLib.USER_ID, sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
            mIntent.putExtra(ConstantLib.FROM, "IMAGE")
            FileUploadService.enqueueWork(this, mIntent)
            val intent = Intent(applicationContext,HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

    }

    private fun setupViewData() {
        binding.txtMymemories.text = languageData!!.klMYMEMORY
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
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        postMemoriesPresenterImplementation!!.onStop()
    }
}
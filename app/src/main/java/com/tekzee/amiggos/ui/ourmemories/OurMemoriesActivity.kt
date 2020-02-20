package com.tekzee.amiggos.ui.ourmemories

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ActivityOurMemoriesBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.ourmemories.fragment.nearby.NearBy
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.OurMemoriesUpload
import com.tekzee.amiggos.ui.ourmemories.service.FileOurMemoryUploadService
import com.tekzee.amiggos.ui.postmemories.service.FileUploadService
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib
import kotlinx.android.synthetic.main.activity_realfriends.*

class OurMemoriesActivity : BaseActivity() {

    private lateinit var binding: ActivityOurMemoriesBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var selectedIds:String =""


    companion object{
        var selectUserIds : ArrayList<Int> = ArrayList()
        var ourMemoryId: String =""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_our_memories)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupViewData()
        LoadFirstFragment()
        ourMemoryId = intent.getStringExtra(ConstantLib.OURSTORYID)
    }

    private fun LoadFirstFragment() {
        LoadFragment(OurMemoriesUpload())
    }

    private fun LoadSecondFragment() {
        LoadFragment(NearBy())
    }

    fun LoadFragment(fragment: Fragment){
        val fm: FragmentManager = supportFragmentManager
        val fragmentTransaction : FragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.container,fragment)
        fragmentTransaction.commit()
    }



    private fun setupViewData() {


        binding.radio2.text = languageData!!.klAMIGGOSNEARME
        binding.radio1.text = languageData!!.klFriendNavTitle


        binding.radio1.isChecked = true

        if(radio1.isChecked){
            binding.radio1.setTextColor(resources.getColor(R.color.white))
        }else{
            binding.radio1.setTextColor(resources.getColor(R.color.colorPrimary))
        }

        if(radio2.isChecked){
            binding.radio2.setTextColor(resources.getColor(R.color.white))
        }else{
            binding.radio2.setTextColor(resources.getColor(R.color.colorPrimary))
        }


        binding.radio1.setOnCheckedChangeListener{
            compoundButton, b ->
            if(b){
                binding.radio1.setTextColor(resources.getColor(R.color.white))
            }else{
                binding.radio1.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }


        binding.radio2.setOnCheckedChangeListener{
                compoundButton, b ->
            if(b){
                binding.radio2.setTextColor(resources.getColor(R.color.white))
            }else{
                binding.radio2.setTextColor(resources.getColor(R.color.colorPrimary))
            }
        }


        binding.radiogroup.setOnCheckedChangeListener{
            radioGroup, checkedId ->

            if(checkedId == R.id.radio1){
                LoadFirstFragment()
            }else{
                LoadSecondFragment()
            }
        }

        binding.btnSave.setOnClickListener{
            for (item in selectUserIds){
                Logger.d("ids: "+item)
             //   selectedIds = selectUserIds+","+item.toString()
            }

            val commaSeperatedString = selectUserIds.joinToString { it -> "${it}" }
            Logger.d("comma: "+commaSeperatedString)
            callUploadImageToMyMemories(commaSeperatedString)
        }
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klOURMEMORY
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



    private fun callUploadImageToMyMemories(commaSeperatedString: String) {
        if (intent.getStringExtra(ConstantLib.FROM).equals("VIDEO", true)) {
            val imageUri = intent.getStringExtra(ConstantLib.FILEURI)
            val mIntent = Intent(this, FileUploadService::class.java)
            mIntent.putExtra(ConstantLib.USER_ID, sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
            mIntent.putExtra(ConstantLib.FROM, "VIDEO")
            mIntent.putExtra(ConstantLib.OURSTORYID, ourMemoryId)
            mIntent.putExtra(ConstantLib.USERIDLIST, commaSeperatedString)
            FileOurMemoryUploadService.enqueueWork(this, mIntent)

            val intent = Intent(applicationContext, AHomeScreen::class.java)
            startActivity(intent)
            selectUserIds.clear()
            finishAffinity()
        }else{
            val imageUri: Uri = intent.getParcelableExtra(ConstantLib.FILEURI) as Uri
            val mIntent = Intent(this, FileUploadService::class.java)
            mIntent.putExtra(ConstantLib.USER_ID, sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
            mIntent.putExtra(ConstantLib.FILEURI, imageUri)
            mIntent.putExtra(ConstantLib.FROM, "IMAGE")
            mIntent.putExtra(ConstantLib.OURSTORYID, ourMemoryId)
            mIntent.putExtra(ConstantLib.USERIDLIST, commaSeperatedString)
            FileOurMemoryUploadService.enqueueWork(this, mIntent)
            val intent = Intent(applicationContext, AHomeScreen::class.java)
            startActivity(intent)
            selectUserIds.clear()
            finishAffinity()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        selectUserIds.clear()
    }

}
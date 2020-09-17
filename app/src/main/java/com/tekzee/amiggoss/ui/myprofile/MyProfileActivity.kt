package com.tekzee.amiggoss.ui.myprofile

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.MyprofileActivityBinding
import com.tekzee.amiggoss.ui.myprofile.model.MyProfileResponse
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import android.content.Intent
import cn.pedant.SweetAlert.SweetAlertDialog
import com.tekzee.amiggoss.cameranew.CameraActivity
import com.tekzee.amiggoss.ui.chat.myfriendchatlist.MyFriendChatActivity
import com.tekzee.amiggoss.ui.imageviewer.ImageViewerActivity
import com.tekzee.amiggoss.ui.mymemories.fragment.ourmemories.model.OurMemoriesResponse
import com.tekzee.amiggoss.ui.storieview.StorieViewActivity


class MyProfileActivity : BaseActivity(), MyProfilePresenter.MyProfileMainView {


    private var responseMyProfileData: OurMemoriesResponse? = null
    private var data: MyProfileResponse? = null
    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private lateinit var binding: MyprofileActivityBinding
    private var myProfilePresenterImplementation: MyProfilePresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.myprofile_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        myProfilePresenterImplementation = MyProfilePresenterImplementation(this, this)
        setupToolBar()
        callMyProfileApi()
        callGetMyStoryByUserId()
        setupViewData()
        setupClickListener()
    }

    private fun callGetMyStoryByUserId() {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        myProfilePresenterImplementation!!.doGetMyStoryByUserId(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun setupClickListener() {

        binding.chatlayout.setOnClickListener {
            val intent = Intent(applicationContext, MyFriendChatActivity::class.java)
            startActivity(intent)
        }

        binding.share.setOnClickListener {
            shareIntent()
        }



        binding.pMyMemories.setOnClickListener {
            val intent = Intent(applicationContext, StorieViewActivity::class.java)
            intent.putExtra(ConstantLib.FROM,"MYPROFILE")
            intent.putExtra(ConstantLib.CONTENT,responseMyProfileData!!.data[0])
            intent.putExtra(ConstantLib.PROFILE_IMAGE,responseMyProfileData!!.data[0].imageUrl)
            intent.putExtra(ConstantLib.USER_ID, responseMyProfileData!!.data[0].userid.toString())
            intent.putExtra(ConstantLib.USER_NAME, responseMyProfileData!!.data[0].name)
            startActivity(intent)
        }

        binding.addmemory.setOnClickListener {
            val intent = Intent(applicationContext, CameraActivity::class.java)
            intent.putExtra(
                ConstantLib.PROFILE_IMAGE,
                sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE)
            )
            intent.putExtra(ConstantLib.FROM_ACTIVITY, "MYPROFILEACTIVITY")
            startActivity(intent)
        }

        binding.pMyId.setOnClickListener {
            val intent = Intent(applicationContext, ImageViewerActivity::class.java)
            intent.putExtra(ConstantLib.PROFILE_IMAGE, data!!.data[0].idProof)
            startActivity(intent)
        }

        binding.pMyReferalCode.setOnClickListener {
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            pDialog.titleText =
                languageData!!.kllblMyrefralCodeTitle + " " + data!!.data[0].referencecode
            pDialog.setCancelable(false)
            pDialog.setConfirmButton(languageData!!.klOk) {
                pDialog.dismiss()
            }
            pDialog.show()
        }


    }

    private fun setupViewData() {
        binding.pMyMemories.text = languageData!!.klMyMemories
        binding.pMyReferalCode.text = languageData!!.kllblMyrefralCodeTitle
        binding.pMyId.text = languageData!!.kllblMyIdTitle


        binding.txtChat.text = languageData!!.klBtnChatTitle
        binding.txtAddmemory.text = languageData!!.kllblAddStoryTitle
        binding.txtShare.text = languageData!!.kllblShareTitle
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klFriendProfileTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun callMyProfileApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        myProfilePresenterImplementation!!.doMyProfile(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun onMyProfileSuccess(responseData: MyProfileResponse?) {
        data = responseData
        binding.fpTxtName.text = responseData!!.data[0].name
        binding.fpTxtDescriptionAge.text = responseData.data[0].location
        binding.fpTxtDescriptionLocation.text = responseData.data[0].age.toString()
        Glide.with(applicationContext).load(responseData.data[0].profile)
            .placeholder(R.drawable.user).into(binding.fpImgProfile)

    }

    override fun onMyStoryByUserIdSuccess(responseData: OurMemoriesResponse?) {
        responseMyProfileData = responseData
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun shareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            sharedPreference!!.getValueString(ConstantLib.INVITE_MESSAGE)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }
}
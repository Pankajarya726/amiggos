package com.tekzee.amiggos.ui.friendprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.FriendProfileBinding
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.kcode.bottomlib.BottomDialog





class FriendProfile : BaseActivity(), FriendProfilePresenter.FriendProfileMainView {


    private lateinit var binding: FriendProfileBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var friendProfilePresenterImplementation: FriendProfilePresenterImplementation? = null
    var data: FriendProfileResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.friend_profile)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        friendProfilePresenterImplementation = FriendProfilePresenterImplementation(this, this)
        setupToolBar()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
        setupViewData()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.fpAccept.setOnClickListener {
            callAcceptApi()
        }

        binding.fpReject.setOnClickListener {
            callRejectApi()
        }

        binding.addRealFriend.setOnClickListener {

            if (data!!.data[0].isRelate != 2) {
                callSendFriendRequest()
            }
        }

        binding.imgOption.setOnClickListener {

            if (data!!.data[0].isRelate == 1 || data!!.data[0].isRelate == 2 || data!!.data[0].isRelate == 3) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(languageData!!.klBlock, languageData!!.lblBtnReport)
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            blockConfirmation()
                        }
                        1 -> {
                            callReport()
                        }

                    }
                }
            } else if (data!!.data[0].isRelate == 4) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(
                        languageData!!.klUnfriend,
                        languageData!!.klBlock,
                        languageData!!.lblBtnReport
                    )
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            unFriendConfirmation()
                        }
                        1 -> {
                            blockConfirmation()
                        }
                        2 -> {
                            callReport()
                        }

                    }
                }
            } else if (data!!.data[0].isRelate == 5) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(
                        languageData!!.klUnfriend,
                        languageData!!.klUnblocked,
                        languageData!!.lblBtnReport
                    )
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            unFriendConfirmation()
                        }
                        1 -> {
                            callUnBlock()
                        }
                        2 -> {
                            callReport()
                        }

                    }
                }
            }

        }
    }

    private fun setupViewData() {
        binding.fpTxtTitleLocation.text = languageData!!.klAgeTitle + " : "
        binding.fpTxtTitleAge.text = languageData!!.klLocationTitle + " : "
        binding.fpChat.text = languageData!!.klBtnChatTitle
        binding.fpOurMemories.text = languageData!!.klOURMEMORY
        binding.addRealFriend.text = languageData!!.klAddFriend
    }


    private fun callFriendProfileApi(friendid: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", friendid)
        friendProfilePresenterImplementation!!.doCallGetFriendProfileApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klFriendProfileTitle2
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun callRejectApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.doRejectInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callSendFriendRequest() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.doSendFriendRequest(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callunFriend() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.callunFriend(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callUnBlock() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.callUnBlock(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callReport() {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "amiggosapp@gamil.com", null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Send from my android phone")
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun callBlock() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.callBlock(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callAcceptApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", data!!.data[0].userid)
        friendProfilePresenterImplementation!!.doAcceptInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    override fun onFriendProfileSuccess(responseData: FriendProfileResponse?) {
        binding.fpTxtName.text = responseData!!.data[0].name
        binding.fpTxtDescriptionAge.text = responseData.data[0].location
        binding.fpTxtDescriptionLocation.text = responseData.data[0].age.toString()
        Glide.with(applicationContext).load(responseData.data[0].profile)
            .placeholder(R.drawable.user).into(binding.fpImgProfile)

        data = responseData

        if (responseData.data[0].isRelateOurMemory.isOurStory == 0) {
            binding.fpOurMemories.visibility = View.GONE
        } else {
            binding.fpOurMemories.visibility = View.VISIBLE
        }


        when (responseData.data[0].isRelate) {
            1 -> {
                binding.addRealFriend.visibility = View.VISIBLE
                binding.layoutAcceptReject.visibility = View.GONE
                binding.addRealFriend.text = languageData!!.klAddFriend
                binding.addRealFriend.isClickable = true
            }
            2 -> {
                binding.addRealFriend.visibility = View.VISIBLE
                binding.layoutAcceptReject.visibility = View.GONE
                binding.addRealFriend.text = languageData!!.klRequested
                binding.addRealFriend.isClickable = false
            }
            3 -> {
                binding.addRealFriend.visibility = View.GONE
                binding.layoutAcceptReject.visibility = View.VISIBLE
                binding.fpAccept.text = languageData!!.klAccept
                binding.fpReject.text = languageData!!.klReject
                binding.addRealFriend.isClickable = true
            }
            4 -> {
                binding.addRealFriend.visibility = View.GONE
                binding.layoutAcceptReject.visibility = View.GONE
                binding.addRealFriend.isClickable = true

            }
            5 -> {
                binding.addRealFriend.visibility = View.GONE
                binding.layoutAcceptReject.visibility = View.GONE
                binding.addRealFriend.isClickable = true
            }
        }


    }

    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    override fun onSendFriendRequestSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun onUnFriendSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    override fun onUnBlockSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    override fun onCallReportSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }


    override fun onBlockSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    fun blockConfirmation(){
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = languageData!!.klAlertMsgBlock
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klBlock) {
            pDialog.dismiss()
            callBlock()
        }
        pDialog.show()
    }






    fun unFriendConfirmation(){
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = languageData!!.klUnfriendAlert
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klUnfriend) {
            pDialog.dismiss()
            callunFriend()
        }
        pDialog.show()
    }


}
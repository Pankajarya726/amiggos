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
import com.tekzee.amiggos.ui.chat.MessageActivity
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.storieview.StorieViewActivity


class FriendProfile : BaseActivity(), FriendProfilePresenter.FriendProfileMainView {


    private var friendName: String? = ""
    private var friendImage: String? = ""
    private lateinit var binding: FriendProfileBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var friendProfilePresenterImplementation: FriendProfilePresenterImplementation? = null
    var data: FriendProfileResponse? = null
    var mDataList: ArrayList<StoriesData> = ArrayList()

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
        binding.fpOurMemories.setOnClickListener {
            callOurMemorieApi()
        }

        binding.fpChat.setOnClickListener{
            val intentActivity = Intent(applicationContext,MessageActivity::class.java)
            intentActivity.putExtra(ConstantLib.FRIEND_ID,intent.getStringExtra(ConstantLib.FRIEND_ID))
            intentActivity.putExtra(ConstantLib.FRIENDNAME,friendName)
            intentActivity.putExtra(ConstantLib.FRIENDIMAGE,friendImage)
            startActivity(intentActivity)
        }


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

    private fun callOurMemorieApi() {
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("our_story_id", intent.getStringExtra(ConstantLib.OURSTORYID))
            friendProfilePresenterImplementation!!.doCallStorieViewApi(
                "",
                input,
                Utility.createHeaders(sharedPreference)
            )

    }

    private fun setupViewData() {
        binding.fpTxtTitleLocation.text = languageData!!.klAgeTitle + " : "
        binding.fpTxtTitleAge.text = languageData!!.klLocationTitle + " : "
        binding.fpChat.text = languageData!!.klBtnChatTitle
        binding.fpOurMemories.text = languageData!!.klOURMEMORY
        binding.addRealFriend.text = languageData!!.klAddFriend
        binding.fpAccept.text = languageData!!.klAccept
        binding.fpReject.text = languageData!!.klReject
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
        friendName = responseData!!.data[0].name
        friendImage = responseData!!.data[0].profile
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

    override fun onStorieSuccess(responseData: StorieResponse, notificationId: String) {
        mDataList = responseData.data as ArrayList<StoriesData>
        gotoStorieView(mDataList,notificationId)
    }


    fun gotoStorieView(
        mDataList: ArrayList<StoriesData>,
        notificationId: String
    ) {
        val intent =Intent(applicationContext, StorieViewActivity::class.java)
        intent.putExtra(ConstantLib.CONTENT, this.mDataList[0])
        intent.putExtra(ConstantLib.PROFILE_IMAGE, this.mDataList[0].imageUrl)
        intent.putExtra(ConstantLib.FROM,"FRIENDPROFILE")
        intent.putExtra(ConstantLib.NOTIFICAION_ID,notificationId)
        intent.putExtra(ConstantLib.USER_ID, this.mDataList[0].userid.toString())
        intent.putExtra(ConstantLib.USER_NAME, this.mDataList[0].name)
        startActivity(intent)
    }

}
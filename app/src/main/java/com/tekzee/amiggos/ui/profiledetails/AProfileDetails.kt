package com.tekzee.amiggos.ui.profiledetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.kcode.bottomlib.BottomDialog
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ProfileDetailsBinding
import com.tekzee.amiggos.ui.favoritevenues.AFavoriteVenues
import com.tekzee.amiggos.ui.friendprofile.FriendProfilePresenter
import com.tekzee.amiggos.ui.friendprofile.FriendProfilePresenterImplementation
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggos.ui.realamiggos.RealAmiggos
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class AProfileDetails : BaseActivity(), FriendProfilePresenter.FriendProfileMainView {
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var friendProfilePresenterImplementation: FriendProfilePresenterImplementation? = null

    private var binding: ProfileDetailsBinding? = null
    private var isMyFriend: Boolean = false
    private var isMyFriendBlocked: Boolean  = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_details)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        friendProfilePresenterImplementation = FriendProfilePresenterImplementation(this, this)
        setupViews()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding!!.imageoptions.setOnClickListener {

            if (!isMyFriend) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(languageData!!.klAddFriend,languageData!!.klBlock, languageData!!.lblBtnReport)
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            callSendFriendRequest()
                        } 1 -> {
                            blockConfirmation()
                        }
                        2 -> {
                            callReport()
                        }

                    }
                }
            } else if (isMyFriend && !isMyFriendBlocked) {
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
            } else if (isMyFriend && isMyFriendBlocked) {
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

    private fun callUnBlock() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", intent.getStringExtra(ConstantLib.FRIEND_ID))
        friendProfilePresenterImplementation!!.callUnBlock(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun setupViews() {
        Glide.with(applicationContext).load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        binding!!.txtName.text = intent.getStringExtra(ConstantLib.NAME)
        binding!!.txtLocation.text = intent.getStringExtra(ConstantLib.ADDRESS)
        binding!!.txtCount.text = intent.getStringExtra(ConstantLib.REAL_FREIND_COUNT)
        val tabs =binding!!.htabTabs
        val viewPager = binding!!.htabViewpager
        setupAdapter(viewPager, tabs)
    }


    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(RealAmiggos(intent.getStringExtra(ConstantLib.FRIEND_ID)), languageData!!.PRealAmiggos)
        adapter.addFragment(AFavoriteVenues(intent.getStringExtra(ConstantLib.FRIEND_ID)), languageData!!.PFavoriteVenue)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onFriendProfileSuccess(responseData: FriendProfileResponse?) {
        Toast.makeText(this,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onFriendProfileV2Success(responseData: GetFriendProfileDetailsResponse?) {
        isMyFriend = responseData!!.data.isMyFriend.toBoolean()
        isMyFriendBlocked = responseData.data.isMyFriendBlocked.toBoolean()
    }

    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(this,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(this,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onSendFriendRequestSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
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
    }

    override fun onBlockSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID))
    }

    override fun onStorieSuccess(responseData: StorieResponse, notificationId: String) {
        TODO("Not yet implemented")
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
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


    private fun callBlock() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", intent.getStringExtra(ConstantLib.FRIEND_ID))
        friendProfilePresenterImplementation!!.callBlock(
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

    private fun callunFriend() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", intent.getStringExtra(ConstantLib.FRIEND_ID))
        friendProfilePresenterImplementation!!.callunFriend(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun callSendFriendRequest() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", intent.getStringExtra(ConstantLib.FRIEND_ID))
        friendProfilePresenterImplementation!!.doSendFriendRequest(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun callFriendProfileApi(friendid: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("freind_id", friendid)
        friendProfilePresenterImplementation!!.doCallGetFriendProfileApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


}
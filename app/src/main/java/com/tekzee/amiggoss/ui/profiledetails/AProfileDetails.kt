package com.tekzee.amiggoss.ui.profiledetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.kcode.bottomlib.BottomDialog
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.base.model.CommonResponse
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ProfileDetailsBinding
import com.tekzee.amiggoss.ui.favoritevenues.AFavoriteVenues
import com.tekzee.amiggoss.ui.friendprofile.FriendProfilePresenter
import com.tekzee.amiggoss.ui.friendprofile.FriendProfilePresenterImplementation
import com.tekzee.amiggoss.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggoss.ui.realamiggos.RealAmiggos
import com.tekzee.amiggoss.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggoss.ui.notification.model.StorieResponse
import com.tekzee.amiggoss.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import kotlinx.android.synthetic.main.referal_activity.*

class AProfileDetails : BaseActivity(), FriendProfilePresenter.FriendProfileMainView {
    private var imageBuilder: StfalconImageViewer<String>?=null
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
        Glide.with(applicationContext).load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        setupClickListener()

        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
        if(intent.getStringExtra(ConstantLib.FRIEND_ID).equals(sharedPreference!!.getValueInt(
                ConstantLib.USER_ID).toString(),true)){
            binding!!.imageoptions.visibility = View.GONE
        }else{
            binding!!.imageoptions.visibility = View.VISIBLE
        }

    }


    override fun onBackPressed() {

        val intent = Intent()
        setResult(2,intent)
        finish()
        Animatoo.animateSlideLeft(this)
    }



    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.htabHeader.setOnClickListener {
            val image = arrayOf(intent.getStringExtra(ConstantLib.PROFILE_IMAGE))
            imageBuilder =
                StfalconImageViewer.Builder<String>(this, image) { imageView, image ->
                        Glide.with(this).load(image).into(imageView)
                    }.withTransitionFrom(imageView)
                    .withBackgroundColor(resources.getColor(R.color.black))
                    .allowSwipeToDismiss(true)
                    .withOverlayView(PosterOverlayView(this).apply {

                        onDeleteClick = {
                            imageBuilder!!.dismiss()
                        }


                    }).show(true)



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
            } else if (!isMyFriend && isMyFriendBlocked) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(
                        languageData!!.klUnblocked,
                        languageData!!.lblBtnReport
                    )
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            callUnBlock()
                        }
                        1 -> {
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


    private fun setupViews(data: GetFriendProfileDetailsResponse.Data) {

        binding!!.txtName.text = data.name +" "+ data.lastName
        binding!!.txtLocation.text = data.address
        binding!!.txtCount.text = data.real_freind_count
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
        binding!!.htabMaincontent.visibility = View.VISIBLE
        isMyFriend = responseData!!.data.isMyFriend.toBoolean()
        isMyFriendBlocked = responseData.data.isMyFriendBlocked.toBoolean()
        Glide.with(applicationContext).load(responseData.data.profile).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        setupViews(responseData.data)
    }

    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(this,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(this,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onSendFriendRequestSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
    }

    override fun onUnFriendSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
    }

    override fun onUnBlockSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
    }

    override fun onCallReportSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
    }

    override fun onBlockSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext, responseData!!.message, Toast.LENGTH_LONG).show()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
    }

    override fun onStorieSuccess(responseData: StorieResponse, notificationId: String) {
        TODO("Not yet implemented")
    }

    override fun validateError(message: String) {
        binding!!.htabMaincontent.visibility = View.VISIBLE
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
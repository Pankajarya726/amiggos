package com.tekzee.amiggos.ui.profiledetails

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.impulsiveweb.galleryview.GalleryView
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.stfalcon.imageviewer.StfalconImageViewer
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialog
import com.tekzee.amiggos.databinding.ProfileDetailsBinding
import com.tekzee.amiggos.ui.chatnew.ChatActivity
import com.tekzee.amiggos.ui.favoritevenues.AFavoriteVenues
import com.tekzee.amiggos.ui.friendprofile.FriendProfilePresenter
import com.tekzee.amiggos.ui.friendprofile.FriendProfilePresenterImplementation
import com.tekzee.amiggos.ui.friendprofile.model.FriendProfileResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.message.model.MyFriendChatModel
import com.tekzee.amiggos.ui.notification.model.StorieResponse
import com.tekzee.amiggos.ui.profiledetails.model.GetFriendProfileDetailsResponse
import com.tekzee.amiggos.ui.profiledetails.model.SliderAdapterExample
import com.tekzee.amiggos.ui.realamiggos.RealAmiggos
import com.tekzee.amiggos.util.AppBarStateChangedListener
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.util.showKeyboard
import java.util.*


class AProfileDetails : BaseActivity(), FriendProfilePresenter.FriendProfileMainView,
    SliderClickListener {
    private var friendId: String? = ""
    private var friendImage: String? =""
    private var friendName: String? = ""
    private lateinit var list: java.util.ArrayList<String>
    private lateinit var adapter: SliderAdapterExample
    private var imageBuilder: StfalconImageViewer<String>?=null
     private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var friendProfilePresenterImplementation: FriendProfilePresenterImplementation? = null

    private var binding: ProfileDetailsBinding? = null
    private var isMyFriend: Boolean = false
    private var isMyFriendBlocked: Boolean  = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_details)
        adapter = SliderAdapterExample(this, this)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        friendProfilePresenterImplementation = FriendProfilePresenterImplementation(this, this)
        setupClickListener()
        callFriendProfileApi(intent.getStringExtra(ConstantLib.FRIEND_ID)!!)
        if(intent.getStringExtra(ConstantLib.FRIEND_ID).equals(
                sharedPreference!!.getValueInt(
                    ConstantLib.USER_ID
                ).toString(), true
            )){
            binding!!.imageoptions.visibility = View.GONE
        }else{
            binding!!.imageoptions.visibility = View.VISIBLE
        }
        setupViewPager()


        binding!!.htabAppbar.addOnOffsetChangedListener(object: AppBarStateChangedListener(){
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if(state!!.name.equals("EXPANDED")){
                    hideKeyboard()
                }
            }

        })

    }


    fun setupScrolling() {
        Log.e("called expanded-->", "-----------------")
        binding!!.htabAppbar.setExpanded(false, true)

    }

    private fun setupViewPager() {
        binding!!.htabHeader.sliderAdapter = adapter
        //binding!!.htabHeader.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding!!.htabHeader.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding!!.htabHeader.indicatorSelectedColor = Color.WHITE
        binding!!.htabHeader.indicatorUnselectedColor = Color.GRAY

    }


    override fun onBackPressed() {

        val intent = Intent()
        setResult(2, intent)
        finish()
        Animatoo.animateSlideLeft(this)
    }



    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }


        binding!!.imgChat.setOnClickListener {

            val myFriendChatModel = MyFriendChatModel()
            myFriendChatModel.name = friendName
            myFriendChatModel.image = friendImage
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(ConstantLib.FRIEND_ID, friendId)
            intent.putExtra(ConstantLib.CHAT_DATA, myFriendChatModel)
            startActivity(intent)
        }


        binding!!.imageoptions.setOnClickListener {


            if (!isMyFriend) {
                val dialog: BottomDialog = BottomDialog.newInstance(
                    "",
                    arrayOf(
                        languageData!!.klAddFriend,
                        languageData!!.klBlock,
                        languageData!!.lblBtnReport
                    )
                )
                dialog.show(supportFragmentManager, "dialog")
                dialog.setListener { position: Int ->
                    when (position) {
                        0 -> {
                            callSendFriendRequest()
                        }
                        1 -> {
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

        friendId = data.userid.toString()
        friendName = data.name
        friendImage = data.profile[0]



        if(data.userid == sharedPreference!!.getValueInt(ConstantLib.USER_ID)){
            binding!!.imgChat.visibility = View.GONE
            binding!!.imageoptions.visibility = View.GONE
            binding!!.imgLike.visibility = View.GONE
        }else{
            if(isMyFriend && !isMyFriendBlocked){
                binding!!.imgChat.visibility = View.VISIBLE
            }else{
                binding!!.imgChat.visibility = View.GONE
            }

            binding!!.imageoptions.visibility = View.VISIBLE
            binding!!.imgLike.visibility = View.VISIBLE
        }
        binding!!.txtName.text = data.name +" "+ data.lastName
        binding!!.txtLocation.text = data.city+", "+data.state
        binding!!.txtCount.text = data.realFreindCount.toString()
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
        adapter.addFragment(
            RealAmiggos(intent.getStringExtra(ConstantLib.FRIEND_ID), this),
            languageData!!.pRealFriends
        )
        //change language
        adapter.addFragment(
            AFavoriteVenues(intent.getStringExtra(ConstantLib.FRIEND_ID), this),
            languageData!!.favoritevenue
        )
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onFriendProfileSuccess(responseData: FriendProfileResponse?) {
        Toast.makeText(this, responseData!!.message, Toast.LENGTH_LONG).show()
    }

    override fun onFriendProfileV2Success(responseData: GetFriendProfileDetailsResponse?) {
        binding!!.htabMaincontent.visibility = View.VISIBLE
        isMyFriend = responseData!!.data.isMyFriend.toBoolean()
        isMyFriendBlocked = responseData.data.isMyFriendBlocked.toBoolean()

//        Glide.with(applicationContext).load(responseData.data.profile).placeholder(R.drawable.blackbg).into(
//            binding!!.htabHeader
//        )
        list = ArrayList()
        list.addAll(responseData.data.profile)
        adapter.renewItems(list)
        setupViews(responseData.data)
    }

    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(this, responseData!!.message, Toast.LENGTH_LONG).show()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(this, responseData!!.message, Toast.LENGTH_LONG).show()
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
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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
        input.addProperty("friend_id", friendid)
        friendProfilePresenterImplementation!!.doCallGetFriendProfileApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun onSliderClicked() {
        GalleryView.show(this, list)
    }


}
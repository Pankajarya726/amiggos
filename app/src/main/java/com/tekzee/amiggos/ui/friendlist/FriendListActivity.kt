package com.tekzee.amiggos.ui.friendlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.FriendlistActivityBinding
import com.tekzee.amiggos.ui.bookingqrcode.GetBookingQrCodeActivity
import com.tekzee.amiggos.ui.friendlist.adapter.FriendListAdapter
import com.tekzee.amiggos.ui.friendlist.model.FriendListData
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FriendListActivity: BaseActivity(), FriendListPresenter.FriendListMainView {

    private var data = ArrayList<FriendListData>()
    private lateinit var adapter: FriendListAdapter
    private lateinit var binding: FriendlistActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var friendListPresenterImplementation: FriendListPresenterImplementation? = null
    private var languageData: LanguageData? = null
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.friendlist_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        friendListPresenterImplementation = FriendListPresenterImplementation(this,this)
        callFriendList("")
        setupToolBar()
        setupRecyclerView()



        //searching
        RxTextView.textChanges(binding.edtSearch) .filter { it.length > 2 }.debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            if(it.isEmpty()){
                callFriendList("")
            }else{
                callFriendList(it.toString())
            }
        }


        binding.btnSave.setOnClickListener{
            Logger.d("bookingid inside friendlist---->"+intent.getStringExtra(ConstantLib.BOOKING_ID))
            val intentActivity = Intent(applicationContext, GetBookingQrCodeActivity::class.java)
            intentActivity.putExtra(ConstantLib.BOOKING_ID,intent.getStringExtra(ConstantLib.BOOKING_ID))
            startActivity(intentActivity)
        }

    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        binding.title.text = languageData!!.klVenues

        binding.btnSave.text = languageData!!.klDone


        binding.imgShare.setOnClickListener{
            shareIntent()
        }
    }

    fun shareIntent(){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            sharedPreferences!!.getValueString(ConstantLib.INVITE_MESSAGE)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecyclerView() {
        binding.friendListRecyclerview.setHasFixedSize(true)
        binding.friendListRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = FriendListAdapter(data,languageData, object : FriendInviteListener {
            override fun onFrienInviteClicked(friendListData: FriendListData) {
                    if(friendListData.isInvited ==0){
                        callInviteFriend(friendListData.userid)
                    }
            }

        })
        binding.friendListRecyclerview.adapter = adapter
    }

    private fun callInviteFriend(userid: Int) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        input.addProperty("friend_id", userid.toString())
        friendListPresenterImplementation!!.doInviteFriend(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun callFriendList(name: String) {

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        input.addProperty("name", name)
        friendListPresenterImplementation!!.doGetFriendList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


    override fun onFriendListSuccess(responseData: FriendListResponse?) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }


    override fun onFriendInviteSuccess(responseData: CommonResponse?) {
        Toast.makeText(applicationContext,responseData!!.message,Toast.LENGTH_LONG).show()
        callFriendList("")
    }


    override fun onBackPressed() {

    }

}
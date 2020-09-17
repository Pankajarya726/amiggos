package com.tekzee.amiggoss.ui.viewfriends

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ViewFriendActivityBinding
import com.tekzee.amiggoss.ui.friendprofile.FriendProfile
import com.tekzee.amiggoss.ui.myprofile.MyProfileActivity
import com.tekzee.amiggoss.ui.viewfriends.adapter.ViewFriendAdapter
import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewData
import com.tekzee.amiggoss.ui.viewfriends.model.StorieViewResponse
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib

class ViewFriendsActivity: BaseActivity(), ViewFriendPresenter.ViewFriendMainView {

    private lateinit var adapter: ViewFriendAdapter
    private lateinit var binding: ViewFriendActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var viewPresenterImplementation: ViewFriendPresenterImplementation? = null
    private var data = ArrayList<StorieViewData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.view_friend_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        viewPresenterImplementation =ViewFriendPresenterImplementation(this,this)
        setupToolBar()
        setupRecyclerView()
        callViewFriendApi()
    }

    private fun callViewFriendApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("story_id", intent.getStringExtra(ConstantLib.STORY))
        input.addProperty("file_id", "0")
        viewPresenterImplementation!!.docallViewFriendApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )

    }


    private fun setupRecyclerView() {
        binding.viewFriendRecyler.setHasFixedSize(true)
        binding.viewFriendRecyler.layoutManager = LinearLayoutManager(this)
        adapter = ViewFriendAdapter(data,sharedPreferences, object : ViewFriendInterface {
            override fun onFriendClicked(
                position: Int,
                storieViewData: StorieViewData
            ) {
                if(sharedPreferences!!.getValueInt(ConstantLib.USER_ID) == storieViewData.userid){
                    val intent = Intent(applicationContext, MyProfileActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(applicationContext, FriendProfile::class.java)
                    intent.putExtra("from","ViewFriend")
                    intent.putExtra(ConstantLib.FRIEND_ID,storieViewData.userid.toString())
                    startActivity(intent)
                }

            }

        })
        binding.viewFriendRecyler.adapter = adapter
    }
    
    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klViewers
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

    override fun onViewFriendSuccess(responseData: StorieViewResponse?) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }

}
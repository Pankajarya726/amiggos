package com.tekzee.amiggos.ui.viewfriends

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.ViewFriendActivityBinding
import com.tekzee.amiggos.ui.viewfriends.adapter.ViewFriendAdapter
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewData
import com.tekzee.amiggos.ui.viewfriends.model.StorieViewResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile

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
        input.addProperty("memory_id", intent.getStringExtra(ConstantLib.STORY))
//        input.addProperty("file_id", "0")
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
                    val intent = Intent(applicationContext, AViewAndEditProfile::class.java)
                    startActivity(intent)
                }else{
//                    val intent = Intent(applicationContext, FriendProfile::class.java)
//                    intent.putExtra("from","ViewFriend")
//                    intent.putExtra(ConstantLib.FRIEND_ID,storieViewData.userid.toString())
//                    startActivity(intent)

                    val intent = Intent(applicationContext, AProfileDetails::class.java)
                    intent.putExtra(ConstantLib.FRIEND_ID, storieViewData.userid.toString())
//                    intent.putExtra(ConstantLib.IS_MY_FRIEND, mydataList[position].isMyFriend)
//                    intent.putExtra(ConstantLib.IS_MY_FRIEND_BLOCKED, mydataList[position].isMyFriendBlocked)
//                    intent.putExtra(ConstantLib.PROFILE_IMAGE, mydataList[position].profile)
//                    intent.putExtra(ConstantLib.NAME, mydataList[position].name)
//                    intent.putExtra(ConstantLib.ADDRESS, mydataList[position].address)
//                    intent.putExtra(ConstantLib.REAL_FREIND_COUNT, mydataList[position].real_freind_count)
                    intent.putExtra("from", "ViewFriend")
                    startActivity(intent)

                }

            }

        })
        binding.viewFriendRecyler.adapter = adapter
    }
    
    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
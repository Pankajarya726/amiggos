package com.tekzee.amiggos.ui.guestlist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.GuestListActivityBinding
import com.tekzee.amiggos.ui.friendlist.FriendListActivity
import com.tekzee.amiggos.ui.guestlist.adapter.GuestListAdapter
import com.tekzee.amiggos.ui.guestlist.interfaces.GuestListInterface
import com.tekzee.amiggos.ui.guestlist.model.GuestListData
import com.tekzee.amiggos.ui.guestlist.model.GuestListResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class GuestListActivity: BaseActivity(), GuestListPresenter.GuestListMainView {


    lateinit var binding: GuestListActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var guestListPresenterImplementation: GuestListPresenterImplementation? = null
    private lateinit var adapter: GuestListAdapter
    private val items: ArrayList<GuestListData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.guest_list_activity)
        guestListPresenterImplementation = GuestListPresenterImplementation(this,this)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupViewName()
        callGuestListApi()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.guestRecyclerview.setHasFixedSize(true)
        binding.guestRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = GuestListAdapter(items, object : GuestListInterface {
            override fun onItemClicked() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        binding.guestRecyclerview.adapter = adapter
    }

    private fun callGuestListApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        guestListPresenterImplementation!!.doCallGuestListApi(input, Utility.createHeaders(sharedPreference))
    }


    private fun setupViewName() {
//        binding.txtLanguageTitle.text = languageData!!.klChooseLanguage
        if(intent.getIntExtra(ConstantLib.IS_PARTY_OWNER,0)==1){
            binding.btnSave.visibility = View.VISIBLE
        }else{
            binding.btnSave.visibility = View.GONE
        }
        binding.btnSave.text = languageData!!.klInviteMore


        binding.btnSave.setOnClickListener{
            val intentActivity = Intent(applicationContext, FriendListActivity::class.java)
            intentActivity.putExtra(ConstantLib.BOOKING_ID,intent.getStringExtra(ConstantLib.BOOKING_ID))
            startActivity(intentActivity)
        }
    }



    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klfriendList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }




    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    override fun onGuestListSuccess(responseData: GuestListResponse?) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }


}
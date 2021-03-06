package com.tekzee.amiggos.ui.blockedusers

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.BlockedUserActivityBinding
import com.tekzee.amiggos.ui.blockedusers.adapter.BlockedUserAdapter
import com.tekzee.amiggos.ui.blockedusers.model.BlockedUserResponse
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import java.util.ArrayList


class ABlockedUser : BaseActivity(), ABlockedUserPresenter.ABlockedUserPresenterMainView {


    private val data = ArrayList<BlockedUserResponse.Data.BlockedUser>()
    private lateinit var adapter: BlockedUserAdapter
    private var binding: BlockedUserActivityBinding? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private lateinit var aBlockedUserImplementation: ABlockedUserImplementation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.blocked_user_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aBlockedUserImplementation = ABlockedUserImplementation(this, this)
        setupToolBar()
        setupRecyclerView()
        callGetBlockedUserApi()
    }

    private fun callGetBlockedUserApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        aBlockedUserImplementation.docallgetblockusers(
            input,
            Utility.createHeaders(sharedPreferences)
        )

    }

    private fun setupRecyclerView() {
        binding!!.blockedUserRecyclerview.setHasFixedSize(true)
        binding!!.blockedUserRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = BlockedUserAdapter(data, languageData, object : UnblockListener {

            override fun onUnblockClicked(blockedUser: BlockedUserResponse.Data.BlockedUser) {
                callUnblockUser(blockedUser.blocked_user_id.toString())
            }

            override fun onItemClicked(blockedUser: BlockedUserResponse.Data.BlockedUser) {
                if (Utility.checkProfileComplete(sharedPreferences)) {
                    val intent = Intent(applicationContext, AProfileDetails::class.java)
                    intent.putExtra(ConstantLib.FRIEND_ID, blockedUser.blocked_user_id.toString())
                    intent.putExtra(ConstantLib.IS_MY_FRIEND, blockedUser.isMyFriend)
                    intent.putExtra(ConstantLib.IS_MY_FRIEND_BLOCKED, blockedUser.isMyFriend)
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, blockedUser.profile)
                    intent.putExtra(ConstantLib.NAME, blockedUser.name)
                    intent.putExtra(ConstantLib.ADDRESS, blockedUser.address)
                    intent.putExtra(ConstantLib.REAL_FREIND_COUNT, blockedUser.real_freind_count)
                    intent.putExtra("from", "BLOCKED USER")
                    startActivity(intent)
                } else {

                    val dialog: BottomDialogExtended =
                        BottomDialogExtended.newInstance(
                            languageData!!.profilecompletedata,
                            arrayOf(languageData!!.yes)
                        )
                    dialog.show(supportFragmentManager, "dialog")
                    dialog.setListener { position ->
                        val intent = Intent(applicationContext, AViewAndEditProfile::class.java)
                        startActivity(intent)
                    }
                }

            }

        })
        binding!!.blockedUserRecyclerview.adapter = adapter
    }

    private fun callUnblockUser(friendid: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", friendid)
        aBlockedUserImplementation.docallunblockusers(
            input,
            Utility.createHeaders(sharedPreferences)
        )

    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    override fun onblockusersuccess(responseData: List<BlockedUserResponse.Data.BlockedUser>) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData)
    }

    override fun onblockuserfailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onunblocksuccess(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        callGetBlockedUserApi()
    }

    override fun onunblockfailure(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        aBlockedUserImplementation.onStop()
    }
}
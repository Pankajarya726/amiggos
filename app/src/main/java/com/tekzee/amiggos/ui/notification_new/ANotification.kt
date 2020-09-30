package com.tekzee.amiggos.ui.notification_new

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ANotificationBinding
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification.FriendRequestNotificationFragment
import com.tekzee.amiggos.ui.notification_new.fragments.memoriesnotification.MemorieNotificationFragment
import com.tekzee.amiggos.ui.notification_new.fragments.partyinvites.PartyInvitesFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib

class ANotification : BaseActivity() {
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var binding: ANotificationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_notification)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupView()
        setupLanguage()
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupLanguage() {
        //change language

        binding!!.heading.text = languageData!!.notification
    }

    private fun setupView() {
        val tabs = findViewById<TabLayout>(R.id.notification_tabs)
        val viewPager =findViewById<ViewPager>(R.id.notification_viewPager)
        setupAdapter(viewPager, tabs)
    }

    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        //change language
        adapter.addFragment(PartyInvitesFragment.newInstance(), languageData!!.partyinvites)
        //change language
        adapter.addFragment(FriendRequestNotificationFragment.newInstance(), languageData!!.friendrequest)
        adapter.addFragment(MemorieNotificationFragment.newInstance(), languageData!!.pMemories)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
package com.tekzee.amiggos.ui.notification_new

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.BaseFragmentActivity
import com.tekzee.amiggos.databinding.ANotificationBinding
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification.FriendRequestNotificationFragment
import com.tekzee.amiggos.ui.notification_new.fragments.memoriesnotification.MemorieNotificationFragment
import com.tekzee.amiggos.ui.notification_new.fragments.partyinvites.PartyInvitesFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.BookingInvitations
import com.tekzee.amiggos.ui.bookings_new.bookings.ABooking
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerFragmentActivityTwoAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter

class ANotification : BaseFragmentActivity() {
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
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
        val viewPager = findViewById<ViewPager2>(R.id.notification_viewPager)
        setupAdapter(viewPager, tabs)
    }

    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {

        val adapter = ViewPagerFragmentActivityTwoAdapter(this)
        adapter.addFragment(PartyInvitesFragment.newInstance(), languageData!!.partyinvites)
        adapter.addFragment(
            FriendRequestNotificationFragment.newInstance(),
            languageData!!.friendrequest
        )
        adapter.addFragment(MemorieNotificationFragment.newInstance(), languageData!!.pMemories)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setUpHeading(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })



        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    PartyInvitesFragment.newInstance()
                    tab.text = languageData!!.partyinvites
                }
                1 -> {
                    FriendRequestNotificationFragment.newInstance()
                    tab.text = languageData!!.friendrequest
                }
                2 -> {
                    MemorieNotificationFragment.newInstance()
                    tab.text = languageData!!.pMemories
                }

            }
        }.attach()
    }

    private fun setUpHeading(selectedTabPosition: Int) {
        if (selectedTabPosition == 0) {
            binding!!.heading.text = languageData!!.partyinvites
        } else if (selectedTabPosition == 1) {
            binding!!.heading.text = languageData!!.friendrequest
        } else if (selectedTabPosition == 2) {
            binding!!.heading.text = languageData!!.pMemories
        }
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
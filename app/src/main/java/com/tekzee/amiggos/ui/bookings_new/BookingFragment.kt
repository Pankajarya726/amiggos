package com.tekzee.amiggos.ui.bookings_new

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.BookingFragmentBinding
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.BookingInvitations
import com.tekzee.amiggos.ui.bookings_new.bookings.ABooking
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.base.BaseFragment


class BookingFragment : BaseFragment() {
    private var binding: BookingFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    companion object {
        private lateinit var mIntent: Intent
        private var mtab: Int = 0
        private val fragment: BookingFragment? = null

        fun newInstance(intent: Intent, tab: Int): BookingFragment {
            mtab = tab
            mIntent = intent
            if (fragment == null) {
                return BookingFragment()
            }
            return fragment
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.booking_fragment, container, false)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViews(view)
        return binding!!.root
    }


    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {

        val adapter = ViewPagerTwoAdapter(this)
        adapter.addFragment(ABooking.newInstance(), languageData!!.klMyBooking)
        adapter.addFragment(BookingInvitations.newInstance(), languageData!!.pInvites)
        viewPager.adapter = adapter
//        viewPager.offscreenPageLimit = 1

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    ABooking.newInstance()
                    tab.text = languageData!!.klMyBooking
                }
                1 -> {
                    BookingInvitations.newInstance()
                    tab.text = languageData!!.pInvites
                }

            }

        }.attach()
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
        viewPager.currentItem = mtab
    }

    private fun setUpHeading(selectedTabPosition: Int) {
        if (selectedTabPosition == 0) {
            binding!!.bookingHeading.text = languageData!!.klMyBooking
        } else if (selectedTabPosition == 1) {
            binding!!.bookingHeading.text = languageData!!.pInvites
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    private fun setupViews(view: View?) {
        setupAdapter(binding!!.bookingviewpager, binding!!.nearMeTabs)
    }


}
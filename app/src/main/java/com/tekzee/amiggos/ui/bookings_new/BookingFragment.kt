package com.tekzee.amiggos.ui.bookings_new

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.BookingInvitations
import com.tekzee.amiggos.ui.bookings_new.bookings.ABooking
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import kotlinx.android.synthetic.main.menu_fragment.*


class BookingFragment : BaseFragment(){

    private var headingTitle: TextView?=null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null



    companion object {
        private lateinit var mIntent: Intent
        private var mtab: Int=0
        private val fragment: BookingFragment? = null
        fun newInstance(intent: Intent, tab: Int): BookingFragment{
            mtab = tab
            mIntent = intent
            if(fragment == null){
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


        val view = inflater.inflate(R.layout.booking_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViews(view)
        setupLanguage(view)
    }


    private fun setupLanguage(view: View?) {
        headingTitle = view!!.findViewById<TextView>(R.id.booking_heading)
    }


    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {

        val adapter = ViewPagerTwoAdapter(this)
        adapter.addFragment(ABooking(), languageData!!.klMyBooking)
        adapter.addFragment(BookingInvitations(), languageData!!.pInvites)
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
                    ABooking()
                    tab.text = languageData!!.klMyBooking
                }
                1 -> {
                    BookingInvitations()
                    tab.text = languageData!!.pInvites
                }

            }
        }.attach()
//        viewpager.currentItem = mtab

//        val fragmentManager = childFragmentManager
//        val adapter = ViewPagerAdapter(fragmentManager)
//        adapter.addFragment(ABooking(), languageData!!.klMyBooking)
//        adapter.addFragment(BookingInvitations(), languageData!!.pInvites)
//        viewPager.adapter = adapter
//        tabs.setupWithViewPager(viewPager)
//        viewPager.currentItem = mtab
    }

    private fun setUpHeading(selectedTabPosition: Int) {
        if(selectedTabPosition ==0){
            headingTitle!!.text = languageData!!.klMyBooking
        }else  if(selectedTabPosition ==1){
            headingTitle!!.text = languageData!!.pInvites
        }
    }
//    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {
//            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//        }
//
//        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)
//            setUpHeading(position)
//        }
//
//        override fun onPageScrollStateChanged(state: Int) {
//            super.onPageScrollStateChanged(state)
//        }
//    })
//    private fun setUpHeading(selectedTabPosition: Int) {
//        if(selectedTabPosition ==0){
//            binding!!.memoriesHeading.text = languageData!!.klMemories
//        }else  if(selectedTabPosition ==1){
//            binding!!.memoriesHeading.text = languageData!!.pourmemories
//        }else if(selectedTabPosition ==2){
//            binding!!.memoriesHeading.text = languageData!!.venues
//        }
//    }

    override fun validateError(message: String) {
       Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
        val tabs = view!!.findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = view!!.findViewById<ViewPager2>(R.id.near_me_viewPager)
        setupAdapter(viewPager,tabs)
    }


}
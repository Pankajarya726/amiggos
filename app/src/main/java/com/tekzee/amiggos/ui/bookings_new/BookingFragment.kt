package com.tekzee.amiggos.ui.bookings_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.BookingInvitations
import com.tekzee.amiggos.ui.bookings_new.bookings.ABooking
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.FirstFragment
import com.tekzee.amiggos.ui.mybooking.MyBookingActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib


class BookingFragment : BaseFragment(){
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null



    companion object {

        fun newInstance(): BookingFragment {
            return BookingFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.booking_fragment, container, false)
       sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupViews(view)
        setupLanguage(view)

        return view;
    }


    private fun setupLanguage(view: View?) {
        view!!.findViewById<TextView>(R.id.booking_heading).text = languageData!!.PBooking

    }


    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = getChildFragmentManager()
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(ABooking(), languageData!!.PUpcoming)
        adapter.addFragment(BookingInvitations(), languageData!!.PInvites)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun validateError(message: String) {
       Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
        val tabs = view!!.findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = view.findViewById<ViewPager>(R.id.near_me_viewPager)
        setupAdapter(viewPager,tabs)
    }


}
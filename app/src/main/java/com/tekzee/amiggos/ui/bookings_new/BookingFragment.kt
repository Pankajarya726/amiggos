package com.tekzee.amiggos.ui.bookings_new

import android.content.Intent
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
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib


class BookingFragment : BaseFragment(){
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null



    companion object {
        private lateinit var mIntent: Intent
        private val fragment: BookingFragment? = null
        fun newInstance(intent: Intent): BookingFragment{
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
        view!!.findViewById<TextView>(R.id.booking_heading).text = languageData!!.pBooking

    }


    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = childFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(ABooking(), languageData!!.klMyBooking)
        adapter.addFragment(BookingInvitations(), languageData!!.pInvites)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        if(mIntent.action == "BOOKING"){
            viewPager.currentItem = 2
        }
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
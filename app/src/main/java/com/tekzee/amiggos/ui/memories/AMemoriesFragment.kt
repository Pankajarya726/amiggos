package com.tekzee.amiggos.ui.memories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.memories.mymemories.MyMemorieFragment
import com.tekzee.amiggos.ui.memories.ourmemories.OurMemorieFragment
import com.tekzee.amiggos.ui.memories_new.venuefragment.VenueFragment
import com.tekzee.amiggos.ui.mybooking.MyBookingActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib


class AMemoriesFragment : BaseFragment() {
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    companion object {

        fun newInstance(): AMemoriesFragment {
            return AMemoriesFragment()
        }

        public const val TOTAL_PAGES = 5
        const val TOTAL_PAGES_VENUE = 5
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.amemories_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupViews(view)
        setupLanguage(view)

        return view;
    }

    private fun setupLanguage(view: View?) {
        view!!.findViewById<TextView>(R.id.memories_heading).text = languageData!!.PMemories

    }

    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = getChildFragmentManager()
        val adapter = ViewPagerAdapter(fragmentManager)

        adapter.addFragment(OurMemorieFragment(), languageData!!.POURMEMORIES)
        adapter.addFragment(MyMemorieFragment(), languageData!!.POURMEMORIES)
        adapter.addFragment(VenueFragment(), languageData!!.PVenue)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1;
        tabs.setupWithViewPager(viewPager)

    }

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupViews(view: View?) {
        val tabs = view!!.findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = view.findViewById<ViewPager>(R.id.near_me_viewPager)
        setupAdapter(viewPager, tabs)
    }


}
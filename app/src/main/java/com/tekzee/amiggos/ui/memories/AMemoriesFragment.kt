package com.tekzee.amiggos.ui.memories

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
import com.tekzee.amiggos.ui.memories.mymemoriesold.OurMemorieFragment
import com.tekzee.amiggos.ui.memories.venuefragment.VenueFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.AmemoriesFragmentBinding
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter
import com.tekzee.amiggos.ui.memories.mymemories.MyMemoriesFragment
import com.tekzee.amiggos.util.Utility
import kotlinx.android.synthetic.main.menu_fragment.*


class AMemoriesFragment : BaseFragment() {
    private var binding: AmemoriesFragmentBinding? = null
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


//        val view = inflater.inflate(R.layout.amemories_fragment, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.amemories_fragment, container, false)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViews(view)
        setupLanguage(view)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setupLanguage(view: View?) {
        binding!!.memoriesHeading.text = languageData!!.pMemories
    }

    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {
        val fragmentManager = childFragmentManager
        val adapter = ViewPagerTwoAdapter(this)
        viewPager.isUserInputEnabled = false
        adapter.addFragment(MyMemoriesFragment.newInstance(), languageData!!.klMemories)
        adapter.addFragment(OurMemorieFragment.newInstance(), languageData!!.pourmemories)
        adapter.addFragment(VenueFragment.newInstance(), languageData!!.venues)
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
                    MyMemoriesFragment.newInstance()
                    tab.text = languageData!!.klMemories
                }
                1 -> {
                    OurMemorieFragment.newInstance()
                    tab.text = languageData!!.pourmemories
                }
                2 -> {
                    VenueFragment.newInstance()
                    tab.text = languageData!!.venues
                }
            }
        }.attach()


    }

    private fun setUpHeading(selectedTabPosition: Int) {
        if(selectedTabPosition ==0){
            binding!!.memoriesHeading.text = languageData!!.klMemories
        }else  if(selectedTabPosition ==1){
            binding!!.memoriesHeading.text = languageData!!.pourmemories
        }else if(selectedTabPosition ==2){
            binding!!.memoriesHeading.text = languageData!!.venues
        }
    }

    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    private fun setupViews(view: View?) {
        setupAdapter(binding!!.nearMeViewPager, binding!!.nearMeTabs)
    }


}
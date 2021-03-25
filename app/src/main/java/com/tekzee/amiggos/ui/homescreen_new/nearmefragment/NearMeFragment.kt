package com.tekzee.amiggos.ui.homescreen_new.nearmefragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.NearMeFragmentBinding
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.FirstFragment
import com.tekzee.amiggos.ui.realfriends.invitations.Invitations
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.base.BaseFragment


class NearMeFragment : BaseFragment() {

    private var binding: NearMeFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var firsttime:Int =0
    companion object {
        private var nearMebadge: TabLayout.Tab? = null
        private var realFriendBadge: TabLayout.Tab? = null
        private var invitationBadge: TabLayout.Tab? = null

        private lateinit var mIntent: Intent
        private var mtab: Int = 0
        private val nearmefragment: NearMeFragment? = null


        fun newInstance(intent: Intent, tab: Int): NearMeFragment {
            mIntent = intent
            mtab = tab
            if (nearmefragment == null) {
                return NearMeFragment()
            }
            return nearmefragment
        }

        fun setNearmeBadge(count: Int) {
            if (count > 0) {
                val badge = nearMebadge!!.orCreateBadge
                badge.number = count
            } else {
                nearMebadge!!.removeBadge()
            }

        }

        fun setRealFriendBadge(count: Int) {
            if (count > 0) {
                val badge = realFriendBadge!!.orCreateBadge
                badge.number = count
            } else {
                realFriendBadge!!.removeBadge()
            }

        }

        fun setInvitationBadge(count: Int) {
            if (count > 0) {
                val badge = invitationBadge!!.orCreateBadge
                badge.number = count
            } else {
                invitationBadge!!.removeBadge()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//      val view = inflater.inflate(R.layout.near_me_fragment, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.near_me_fragment, container, false)
        sharedPreference = SharedPreference(requireActivity())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViews(view)
        return binding!!.root
    }


    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {
        val adapter = ViewPagerTwoAdapter(this)
        viewPager.isUserInputEnabled = false
        adapter.addFragment(FirstFragment.newInstance(), languageData!!.pNearme)
        adapter.addFragment(RealFriend.newInstance(), languageData!!.pRealFriends)
        adapter.addFragment(Invitations.newInstance(), languageData!!.pInvitaion)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit =1

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    FirstFragment.newInstance()
                    tab.text = languageData!!.pNearme
                    nearMebadge = tab
                    setNearmeBadge(HomeFragment.staticNearMeBadgeCount)

                }
                1 -> {
                    RealFriend.newInstance()
                    tab.text = languageData!!.pRealFriends
                    realFriendBadge = tab
                    setRealFriendBadge(HomeFragment.staticReaFriendBadgeCount)

                }
                2 -> {
                    Invitations.newInstance()
                    tab.text = languageData!!.pInvitaion
                    invitationBadge = tab
                    setInvitationBadge(HomeFragment.staticRequestBadgeCount)
                }
            }
        }.attach()

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
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
                AHomeScreen.setupNearByCountBadge(/*HomeFragment.staticRequestBadgeCount + HomeFragment.staticReaFriendBadgeCount  + */HomeFragment.staticNearMeBadgeCount)
                if(position ==1 || position ==2){
                    firsttime++
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

            }
        })
        viewPager.currentItem = mtab
    }

    private fun setUpHeading(selectedTabPosition: Int) {
        if (selectedTabPosition == 0) {
            binding!!.nearMeHeading.text = languageData!!.pNearme
            if(firsttime>0){
                HomeFragment.staticNearMeBadgeCount = 0
                setNearmeBadge(0)
            }
        } else if (selectedTabPosition == 1) {
            binding!!.nearMeHeading.text = languageData!!.pRealFriends
            HomeFragment.staticReaFriendBadgeCount =0
            setRealFriendBadge(0)
        } else if (selectedTabPosition == 2) {
            binding!!.nearMeHeading.text = languageData!!.pInvitaion
            HomeFragment.staticRequestBadgeCount = 0
            setInvitationBadge(0)
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    private fun setupViews(view: View?) {
        setupAdapter(binding!!.nearMeViewPager, binding!!.nearMeTabs)
    }

}


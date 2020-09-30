package com.tekzee.amiggos.ui.homescreen_new.nearmefragment

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
import com.tekzee.amiggos.databinding.NearMeFragmentBinding
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerTwoAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.FirstFragment
import com.tekzee.amiggos.ui.realfriends.invitations.Invitations
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment


class NearMeFragment : BaseFragment() {
    private var binding: NearMeFragmentBinding? =null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    companion object {

        private lateinit var mIntent: Intent
        private val nearmefragment: NearMeFragment? = null

        fun newInstance(intent: Intent): NearMeFragment{
            mIntent = intent
            if(nearmefragment == null){
                return NearMeFragment()
            }
            return nearmefragment
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
//         val view = inflater.inflate(R.layout.near_me_fragment, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.near_me_fragment, container, false)
        sharedPreference = SharedPreference(requireActivity())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupViews(view)
        setupLanguage(view)
        return binding!!.root
    }

    private fun setupLanguage(view: View?) {

        binding!!.nearMeHeading.text = languageData!!.pNearme
    }

    private fun setupAdapter(
        viewPager: ViewPager2,
        tabs: TabLayout
    ) {
        val fragmentManager = childFragmentManager
        val adapter = ViewPagerTwoAdapter(this)
        adapter.addFragment(FirstFragment.newInstance(), languageData!!.pNearme)
        adapter.addFragment(RealFriend.newInstance(), languageData!!.pRealFriends)
        adapter.addFragment(Invitations.newInstance(), languageData!!.pInvitaion)
        viewPager.adapter = adapter
//        tabs.setupWithViewPager(viewPager)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    FirstFragment.newInstance()
                    tab.text = languageData!!.pNearme
                }
                1 -> {
                    RealFriend.newInstance()
                    tab.text = languageData!!.pRealFriends
                }
                2 -> {
                    Invitations.newInstance()
                    tab.text = languageData!!.pInvitaion
                }
            }
        }.attach()

        if(mIntent.action == FriendsAction.CLICK.action){
            viewPager.currentItem = 2
         }
    }

    override fun validateError(message: String) {
       Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
        setupAdapter(binding!!.nearMeViewPager, binding!!.nearMeTabs)
    }


}
package com.tekzee.amiggos.ui.homescreen_new.nearmefragment

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
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.FirstFragment
import com.tekzee.amiggos.ui.realfriends.invitations.Invitations
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.enums.FriendsAction


class NearMeFragment : BaseFragment() {
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
         val view = inflater.inflate(R.layout.near_me_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupViews(view)
        setupLanguage(view)
        return view
    }

    private fun setupLanguage(view: View?) {
        view!!.findViewById<TextView>(R.id.near_me_heading).text = languageData!!.PNearme
    }

    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = childFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(FirstFragment.newInstance(), languageData!!.PNearme)
        adapter.addFragment(RealFriend.newInstance(), languageData!!.PRealFriends)
        adapter.addFragment(Invitations.newInstance(), languageData!!.PInvitaion)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        if(mIntent.action == FriendsAction.CLICK.action){
            viewPager.currentItem = 2
         }
    }

    override fun validateError(message: String) {
       Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
        val tabs = view!!.findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = view.findViewById<ViewPager>(R.id.near_me_viewPager)
        setupAdapter(viewPager, tabs)

    }


}
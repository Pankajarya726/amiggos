package com.tekzee.amiggos.ui.homescreen_new.nearmefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.FirstFragment
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.secondfragment.SecondFragment
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.thirdfragment.ThirdFragment
import com.tekzee.amiggos.ui.realfriends.invitations.Invitations
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.mallortaxi.base.BaseFragment



class NearMeFragment : BaseFragment(){


    companion object {

        fun newInstance(): NearMeFragment {
            return NearMeFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.near_me_fragment, container, false)
        setupViews(view)


        return view;
    }

    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = getChildFragmentManager()
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(FirstFragment(), "Near Me")
        adapter.addFragment(RealFriend(), "Real Friends")
        adapter.addFragment(Invitations(), "Invitations")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupViews(view: View?) {
        val tabs = view!!.findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = view.findViewById<ViewPager>(R.id.near_me_viewPager)
        setupAdapter(viewPager,tabs)
    }


}
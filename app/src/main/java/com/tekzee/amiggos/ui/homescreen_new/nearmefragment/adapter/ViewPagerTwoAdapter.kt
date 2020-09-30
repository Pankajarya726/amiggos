package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerTwoAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val mFragmentList = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return mFragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragmentList.get(position)
    }


    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
    }
}
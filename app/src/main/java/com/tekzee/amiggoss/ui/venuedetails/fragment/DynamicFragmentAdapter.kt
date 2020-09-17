package com.tekzee.amiggoss.ui.venuedetails.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tekzee.amiggoss.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.amiggoss.constant.ConstantLib

class DynamicFragmentAdapter(
    fm: FragmentManager,
    var NumOfTabs: Int,
    var data: ClubDetailResponse.Data,
    var clubid: String
) : FragmentStatePagerAdapter(fm,NumOfTabs) {


    override fun getItem(position: Int): Fragment {
        val bundle: Bundle = Bundle()
        bundle.putInt("position",position)
        bundle.putString(ConstantLib.CLUB_ID,clubid)

        bundle.putSerializable(ConstantLib.VENUE_DATA,data.clubData[position])
        val fragment: DynamicFragment = DynamicFragment.newInstance()
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return NumOfTabs
    }
}
package com.tekzee.amiggos.ui.venuedetails.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.mallortaxiclient.constant.ConstantLib

class DynamicFragmentAdapter(
    fm: FragmentManager,
    var NumOfTabs: Int,
    var data: VenueDetailResponse,
    var clubid: String
) : FragmentStatePagerAdapter(fm,NumOfTabs) {


    override fun getItem(position: Int): Fragment {
        val bundle: Bundle = Bundle()
        bundle.putInt("position",position)
        bundle.putString(ConstantLib.CLUB_ID,clubid)
        Logger.d("Position----------->"+position)
        bundle.putSerializable(ConstantLib.VENUE_DATA,data.data[position])
        val fragment: DynamicFragment = DynamicFragment.newInstance()
        fragment.arguments = bundle
        return fragment
    }

    override fun getCount(): Int {
        return NumOfTabs
    }
}
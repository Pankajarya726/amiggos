package com.tekzee.amiggos.ui.profiledetails

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ProfileDetailsBinding
import com.tekzee.amiggos.ui.favoritevenues.AFavoriteVenues
import com.tekzee.amiggos.ui.realamiggos.RealAmiggos
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.realfriends.realfriendfragment.RealFriend
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib

class AProfileDetails : BaseActivity(){
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    private var binding: ProfileDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_details)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupViews()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun setupViews() {
        Glide.with(applicationContext).load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE)).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        binding!!.txtName.text = intent.getStringExtra(ConstantLib.NAME)
        binding!!.txtLocation.text = intent.getStringExtra(ConstantLib.ADDRESS)
        binding!!.txtCount.text = intent.getStringExtra(ConstantLib.REAL_FREIND_COUNT)
        val tabs =binding!!.htabTabs
        val viewPager = binding!!.htabViewpager
        setupAdapter(viewPager, tabs)
    }


    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(RealAmiggos(), languageData!!.PRealAmiggos)
        adapter.addFragment(AFavoriteVenues(), languageData!!.PFavoriteVenue)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
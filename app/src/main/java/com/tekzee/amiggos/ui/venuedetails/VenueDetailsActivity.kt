package com.tekzee.amiggos.ui.venuedetails

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.VenueDetailsActivityBinding
import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
import com.tekzee.amiggos.ui.venuedetails.fragment.DynamicFragmentAdapter
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib


class VenueDetailsActivity: BaseActivity(), VenueDetailsPresenter.VenueDetailsMainView {

    private lateinit var data: VenueDetailResponse
    private lateinit var binding: VenueDetailsActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var venueDetailsPresenterImplementation: VenueDetailsPresenterImplementation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.venue_details_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        venueDetailsPresenterImplementation = VenueDetailsPresenterImplementation(this,this)
        data =intent.getSerializableExtra(ConstantLib.VENUE_DATA) as VenueDetailResponse
        setupToolBar()
        initView()
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klVenues
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun initView() {
        binding.viewpager.offscreenPageLimit = 3
        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                Logger.d("TAB RESELECTED")

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

                Logger.d("TAB UNSELECTED")

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewpager.currentItem = tab!!.position


            }
        })

        setDynamicFragmentToTabLayout()
    }

    private fun setDynamicFragmentToTabLayout() {
        for (item in data.data){
            binding.tabs.addTab(binding.tabs.newTab().setText(item.value))
        }

        val dynamicFragmentAdapter: DynamicFragmentAdapter = DynamicFragmentAdapter(supportFragmentManager,binding.tabs.tabCount,data,intent.getStringExtra(ConstantLib.CLUB_ID))
        binding.viewpager.adapter = dynamicFragmentAdapter
        binding.viewpager.currentItem = 0
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


}
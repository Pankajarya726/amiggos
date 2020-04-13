package com.tekzee.amiggos.ui.venuedetails

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.VenueDetailsActivityBinding
import com.tekzee.amiggos.ui.venuedetails.fragment.DynamicFragmentAdapter
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.ui.calendarview.CalendarViewActivity
import com.tekzee.amiggos.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib


class VenueDetailsActivity: BaseActivity(), VenueDetailsPresenter.VenueDetailsMainView {
    private var response: ChooseWeekResponse? = null
    private var data: ClubDetailResponse.Data?=null
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
        data =intent.getSerializableExtra(ConstantLib.VENUE_DATA) as ClubDetailResponse.Data
        callChooseWeekApi()
        setupToolBar()
        setupClickListener()
        initView()
    }

    private fun callChooseWeekApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", intent.getStringExtra(ConstantLib.CLUB_ID))
        venueDetailsPresenterImplementation!!.doCallWeekApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun setupClickListener() {
        binding.txtBook.setOnClickListener{
            val intentActivity = Intent(applicationContext, CalendarViewActivity::class.java)
            intentActivity.putExtra(ConstantLib.CALENDAR_DATA,response)
            intentActivity.putExtra(ConstantLib.CLUB_ID,intent.getStringExtra(ConstantLib.CLUB_ID))
            startActivity(intentActivity)
        }
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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
        for (item in data!!.clubData){
            binding.tabs.addTab(binding.tabs.newTab().setText(item.value))
        }

        val dynamicFragmentAdapter: DynamicFragmentAdapter = DynamicFragmentAdapter(supportFragmentManager,binding.tabs.tabCount,data!!,intent.getStringExtra(
            ConstantLib.CLUB_ID))
        binding.viewpager.adapter = dynamicFragmentAdapter
        binding.viewpager.currentItem = 0
    }

    override fun onChooseWeekSuccess(responseData: ChooseWeekResponse?) {
        response = responseData

    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


}
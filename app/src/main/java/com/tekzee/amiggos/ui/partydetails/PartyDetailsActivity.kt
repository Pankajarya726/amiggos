package com.tekzee.amiggos.ui.partydetails

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.PartyDetailsActivityBinding
import com.tekzee.amiggos.ui.partydetails.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.PartyInvitesFragment
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.PastPartyFragment
import com.tekzee.amiggos.ui.partydetails.fragment.upcommingparty.UpcommingPartyFragment

import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.constant.ConstantLib

class PartyDetailsActivity: BaseActivity() {

    lateinit var binding: PartyDetailsActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.party_details_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupAdapter()
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klPartyDetails
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun validateError(message: String) {

    }

        private fun setupAdapter() {

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PartyInvitesFragment(),languageData!!.klpartyInvites)
        adapter.addFragment(UpcommingPartyFragment(),languageData!!.klupcomingParty)
        adapter.addFragment(PastPartyFragment(),languageData!!.klpastParty)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }
}
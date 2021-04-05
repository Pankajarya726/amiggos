package com.tekzee.amiggos.ui.helpcenter

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.SettingsActivityBinding
import com.tekzee.amiggos.ui.helpcenter.adapter.HelpCenterAdapter
import com.tekzee.amiggos.ui.helpcenter.interfaces.HelpCenterInterface
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterData
import com.tekzee.amiggos.ui.helpcenter.model.HelpCenterResponse
import com.tekzee.amiggos.ui.pages.WebViewActivity
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class HelpCenterActivity: BaseActivity(), HelpCenterPresenter.HelpCenterMainView {

    private lateinit var binding: SettingsActivityBinding
    private lateinit var adapter: HelpCenterAdapter
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var helpCenterPresenterImplementation: HelpCenterPresenterImplementation? = null
    private var data = ArrayList<HelpCenterData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        helpCenterPresenterImplementation = HelpCenterPresenterImplementation(this,this)
        CallHelpCenterApi()
        setupToolBar()
        setupRecyclerView()
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


    private fun setupRecyclerView() {
        binding.settingsRecyclerview.setHasFixedSize(true)
        binding.settingsRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = HelpCenterAdapter(data,object : HelpCenterInterface {
            override fun onHelpCenterClicked(helpCenterData: HelpCenterData) {
                val intent = Intent(applicationContext, WebViewActivity::class.java)
                intent.putExtra(ConstantLib.PAGEURL, helpCenterData.url)
                startActivity(intent)
            }

        })
        binding.settingsRecyclerview.adapter = adapter
    }



    private fun CallHelpCenterApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        helpCenterPresenterImplementation!!.doCallHelpCenterApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }





    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    override fun onHelpCenterSuccess(responseData: HelpCenterResponse?) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }


}
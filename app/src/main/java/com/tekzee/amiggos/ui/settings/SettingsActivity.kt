package com.tekzee.amiggos.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.pages.WebViewActivity
import com.tekzee.amiggos.ui.settings.adapter.SettingsAdapter
import com.tekzee.amiggos.ui.settings.interfaces.SettingsInterface
import com.tekzee.amiggos.ui.settings.model.UpdateSettingsResponse
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.SettingsActivityBinding
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.settings.model.SettingsResponse

class SettingsActivity: BaseActivity(), SettingsPresenter.SettingsMainView {

    private lateinit var binding: SettingsActivityBinding
    private lateinit var adapter:SettingsAdapter
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var settingsPresenterImplementation: SettingsPresenterImplementation? = null
    private var data = ArrayList<SettingsResponse.Data.Setting>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        settingsPresenterImplementation = SettingsPresenterImplementation(this,this)
        callSettingsApi()
        setupToolBar()
        setupRecyclerView()
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.notificationtitle.text = languageData!!.pnotificationandsettins

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
        adapter = SettingsAdapter(data,object : SettingsInterface {
            override fun onTextClicked(settingsData: SettingsResponse.Data.Setting) {
                val intent = Intent(applicationContext, WebViewActivity::class.java)
                intent.putExtra(ConstantLib.PAGEURL, settingsData.url)
                startActivity(intent)
            }

            override fun onItemClicked(
                settingsData: SettingsResponse.Data.Setting,
                flag: Boolean
            ) {
                if(flag){
                    callUpdateSettings("1",settingsData)
                }else{
                    callUpdateSettings("2",settingsData)
                }

            }
        })
        binding.settingsRecyclerview.adapter = adapter
    }

    private fun callUpdateSettings(flag: String, settingsData: SettingsResponse.Data.Setting) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("setting_id", settingsData.id)
        input.addProperty("setting_value", flag)
        settingsPresenterImplementation!!.doUpdateSettings(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }


    private fun callSettingsApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        settingsPresenterImplementation!!.doCallSettingsApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onSettingsSuccess(responseData: SettingsResponse?) {
        data.clear()
        adapter.notifyDataSetChanged()
        data.addAll(responseData!!.data.setting)
        adapter.notifyDataSetChanged()
    }



    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    override fun onUpdateSettingsSuccess(responseData: UpdateSettingsResponse) {
        Toast.makeText(applicationContext,responseData.message,Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenterImplementation!!.onStop()
    }

}
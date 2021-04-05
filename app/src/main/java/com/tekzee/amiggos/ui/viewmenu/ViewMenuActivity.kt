package com.tekzee.amiggos.ui.viewmenu

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ViewmenuActivityBinding
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import com.tekzee.amiggos.ui.viewmenu.adapter.ViewMenuAdapter
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility

class ViewMenuActivity : BaseActivity(){

    private lateinit var data: BookingDetailsNewResponse.Data.Booking
    lateinit var binding : ViewmenuActivityBinding
    private var adapter: ViewMenuAdapter? = null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.viewmenu_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        data = intent.getSerializableExtra(ConstantLib.VIEWMENUDATA) as BookingDetailsNewResponse.Data.Booking
        binding.turningupRecyclerview.setHasFixedSize(true)
        binding.turningupRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = ViewMenuAdapter(data.menus,sharedPreferences,languageData)
        binding.turningupRecyclerview.adapter = adapter
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.heading.text = languageData!!.menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun validateError(message: String) {
        Errortoast(message)
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }
}
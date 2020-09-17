package com.tekzee.amiggoss.ui.chooseweek

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ChooseWeekBinding
import com.tekzee.amiggoss.ui.calendarview.CalendarViewActivity
import com.tekzee.amiggoss.ui.choosepackage.ChoosePackageActivity
import com.tekzee.amiggoss.ui.chooseweek.adapter.ChooseWeekAdapter
import com.tekzee.amiggoss.ui.chooseweek.interfaces.ChooseWeekInterface
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggoss.ui.chooseweek.model.DayData
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib

class ChooseWeekActivity: BaseActivity(), ChooseWeekPresenter.ChooseWeekMainView {

    private var response: ChooseWeekResponse? = null
    private lateinit var adapter: ChooseWeekAdapter
    private lateinit var binding: ChooseWeekBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var chooseWeekPresenterImplementation: ChooseWeekPresenterImplementation? =null
    private var data = ArrayList<DayData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.choose_week)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        chooseWeekPresenterImplementation = ChooseWeekPresenterImplementation(this,this)
        setupToolBar()
        callChooseWeekApi()
        setupRecyclerView()
        initView()
    }

    private fun initView() {
        binding.btnCalendar.text = languageData!!.klbtncalendar
        binding.txtSelectYourDay.text = languageData!!.klselectDaystitle

        binding.btnCalendar.setOnClickListener{
            val intentActivity = Intent(applicationContext, CalendarViewActivity::class.java)
            intentActivity.putExtra(ConstantLib.CALENDAR_DATA,response)
            intentActivity.putExtra(ConstantLib.CLUB_ID,intent.getStringExtra(ConstantLib.CLUB_ID))
            startActivity(intentActivity)
        }
    }


    private fun setupRecyclerView() {
        binding.chooseRecyclerview.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.chooseRecyclerview.layoutManager = layoutManager
        adapter = ChooseWeekAdapter(data,object: ChooseWeekInterface {
            override fun onClickWeek(dayData: DayData) {
                val intent = Intent(applicationContext, ChoosePackageActivity::class.java)
                intent.putExtra(ConstantLib.CLUB_ID,getIntent().getStringExtra(ConstantLib.CLUB_ID))
                intent.putExtra(ConstantLib.PACKAGE_DATE,dayData.date)
                startActivity(intent)
            }
        })
        binding.chooseRecyclerview.adapter = adapter
    }

    private fun callChooseWeekApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", intent.getStringExtra(ConstantLib.CLUB_ID))
        chooseWeekPresenterImplementation!!.doCallWeekApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
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

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    override fun onChooseWeekSuccess(responseData: ChooseWeekResponse?) {
        response = responseData
        data.addAll(responseData!!.data.dayData)
        adapter.notifyDataSetChanged()
    }

}
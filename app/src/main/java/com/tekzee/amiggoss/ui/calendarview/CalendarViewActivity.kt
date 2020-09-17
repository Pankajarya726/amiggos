package com.tekzee.amiggoss.ui.calendarview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.orhanobut.logger.Logger
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ActivityCalendarBinding
import com.tekzee.amiggoss.ui.choosepackage.ChoosePackageActivity
import com.tekzee.amiggoss.ui.chooseweek.model.ChooseWeekResponse
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib
import java.util.*
import kotlin.collections.ArrayList

class CalendarViewActivity: BaseActivity() {

    private lateinit var data: ChooseWeekResponse
    private lateinit var binding: ActivityCalendarBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    val disabledDates = ArrayList<Calendar>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        binding.calendarView.setMinimumDate(cal)


        data =intent.getSerializableExtra(ConstantLib.CALENDAR_DATA) as ChooseWeekResponse



        for (items in data.data.calender)
        {
            if(items.isOpen == 0){
                val cal: Calendar = Calendar.getInstance()
                cal.set(Calendar.YEAR,items.date.split("-")[0].toInt())
                cal.set(Calendar.MONTH,items.date.split("-")[1].toInt()-1)
                cal.set(Calendar.DAY_OF_MONTH,items.date.split("-")[2].toInt())
                disabledDates.add(cal)
            }
        }
        binding.calendarView.setDisabledDays(disabledDates)
        
        binding.calendarView.setOnDayClickListener{
            eventDay ->

            val day = eventDay.calendar.get(Calendar.DAY_OF_MONTH).toString()
            val month = (eventDay.calendar.get(Calendar.MONTH)+1).toString()
            val year = eventDay.calendar.get(Calendar.YEAR).toString()

            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_MONTH, -1)

            if(eventDay.calendar.compareTo(cal)> 0){
                if(!checkDisabledDate(eventDay.calendar)){
                    val intentActivity = Intent(applicationContext,ChoosePackageActivity::class.java)
                    intentActivity.putExtra(ConstantLib.CLUB_ID,intent.getStringExtra(ConstantLib.CLUB_ID))
                    intentActivity.putExtra(ConstantLib.PACKAGE_DATE,year+"-"+month+"-"+day)
                    startActivity(intentActivity)
                }
                else{
                    val pDialog = SweetAlertDialog(this@CalendarViewActivity, SweetAlertDialog.ERROR_TYPE)
                    pDialog.titleText = "This slot is unavailable,Please choose another date"
                    pDialog.setCancelable(false)
                    pDialog.setConfirmButton(languageData!!.klDismiss) {
                        pDialog.dismiss()
                    }
                    pDialog.show()
                }
            } else{
                Logger.d("date is smaller than current date")
            }



        }

    }

    private fun checkDisabledDate(eventDay: Calendar): Boolean {
        var flag: Boolean = false
        for(items in disabledDates){
            if(eventDay.compareTo(items)==0){
                flag = true
            }
        }
        return flag

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


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
package com.tekzee.amiggos.ui.calendarview

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ActivityCalendarBinding
import com.tekzee.amiggos.ui.calendarview.adapter.TimeAdapter
import com.tekzee.amiggos.ui.choosepackage.ChoosePackageActivity
import com.tekzee.amiggos.util.SharedPreference
import java.util.*
import kotlin.collections.ArrayList


class CalendarViewActivity : BaseActivity() {

    private lateinit var adapter: TimeAdapter

    //    private lateinit var data: ChooseWeekResponse
    private lateinit var binding: ActivityCalendarBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    val disabledDates = ArrayList<Calendar>()
    var timedata = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        binding.calendarView.setMinimumDate(cal)

        setupClickListener()

        timedata.add("2pm")
        timedata.add("2pm")
        timedata.add("2pm")
        timedata.add("2pm")
        timedata.add("2pm")
        timedata.add("2pm")
        setupAdapter()
//      data =intent.getSerializableExtra(ConstantLib.CALENDAR_DATA) as ChooseWeekResponse


//        for (items in data.data.calender)
//        {
//            if(items.isOpen == 0){
//                val cal: Calendar = Calendar.getInstance()
//                cal.set(Calendar.YEAR,items.date.split("-")[0].toInt())
//                cal.set(Calendar.MONTH,items.date.split("-")[1].toInt()-1)
//                cal.set(Calendar.DAY_OF_MONTH,items.date.split("-")[2].toInt())
//                disabledDates.add(cal)
//            }
//        }
//        binding.calendarView.setDisabledDays(disabledDates)


    }

    private fun setupAdapter() {
        binding.timeRecycler.layoutManager = GridLayoutManager(this, 3)
        adapter = TimeAdapter(this, timedata)
        binding.timeRecycler.adapter = adapter

    }

    private fun setupClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }

        binding.calendarView.setOnDayClickListener { eventDay ->

            val day = eventDay.calendar.get(Calendar.DAY_OF_MONTH).toString()
            val month = (eventDay.calendar.get(Calendar.MONTH) + 1).toString()
            val year = eventDay.calendar.get(Calendar.YEAR).toString()

            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.YEAR, eventDay.calendar.get(Calendar.YEAR))
            cal.set(Calendar.MONTH, eventDay.calendar.get(Calendar.MONTH))
            cal.set(Calendar.DAY_OF_MONTH, eventDay.calendar.get(Calendar.DAY_OF_MONTH))

//            val listofDate = ArrayList<Calendar>()
//            listofDate.add(cal)
//            binding.calendarView.selectedDates = listofDate

            binding.calendarView.setDate(cal)





//            val cal = Calendar.getInstance()
//            cal.add(Calendar.DAY_OF_MONTH, -1)

            /*if(eventDay.calendar.compareTo(cal)> 0){
                if(!checkDisabledDate(eventDay.calendar)){
                    val intentActivity = Intent(
                        applicationContext,
                        ChoosePackageActivity::class.java
                    )
                    intentActivity.putExtra(
                        ConstantLib.CLUB_ID,
                        intent.getStringExtra(ConstantLib.CLUB_ID)
                    )
                    intentActivity.putExtra(
                        ConstantLib.PACKAGE_DATE,
                        year + "-" + month + "-" + day
                    )
                    startActivity(intentActivity)
                }
                else{
                    val pDialog = SweetAlertDialog(
                        this@CalendarViewActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                    pDialog.titleText = "This slot is unavailable,Please choose another date"
                    pDialog.setCancelable(false)
                    pDialog.setConfirmButton(languageData!!.klDismiss) {
                        pDialog.dismiss()
                    }
                    pDialog.show()
                }
            } else{
                Logger.d("date is smaller than current date")
            }*/
        }
    }

    private fun checkDisabledDate(eventDay: Calendar): Boolean {
        var flag: Boolean = false
        for (items in disabledDates) {
            if (eventDay.compareTo(items) == 0) {
                flag = true
            }
        }
        return flag

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
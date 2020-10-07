package com.tekzee.amiggos.ui.calendarview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ActivityCalendarBinding
import com.tekzee.amiggos.ui.calendarview.adapter.TimeAdapter
import com.tekzee.amiggos.ui.calendarview.model.TimeSlotResponse
import com.tekzee.amiggos.ui.menu.MenuActivity
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalendarViewActivity : BaseActivity(), CalendarViewPresenter.CalendarMainView {

    private lateinit var dataVenue: VenueDetails.Data
    private lateinit var adapter: TimeAdapter

    //    private lateinit var data: ChooseWeekResponse
    private lateinit var binding: ActivityCalendarBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    val disabledDates = ArrayList<Calendar>()
    var timedata = ArrayList<String>()
    var selectedDate = ""
    var seletctedTime = ""
    private var calendarviewpresenterimplementation: CalendarViewPresenterImplementation? = null

    var displayFormat = SimpleDateFormat("HH:mm")
    var parseFormat = SimpleDateFormat("hh:mm a")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        calendarviewpresenterimplementation = CalendarViewPresenterImplementation(this, this)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        binding.calendarView.setMinimumDate(cal)

        val calTodayDate = Calendar.getInstance()
        val day = calTodayDate.get(Calendar.DAY_OF_MONTH).toString()
        val month = (calTodayDate.get(Calendar.MONTH) + 1).toString()
        val year = calTodayDate.get(Calendar.YEAR).toString()

        selectedDate = "$year-$month-$day"
        setupClickListener()
        binding.timepicker.setIs24HourView(false)

        setupAdapter()

        dataVenue = intent.getSerializableExtra(ConstantLib.CALENDAR_DATA) as VenueDetails.Data
        checkTimeSlot()
    }

    private fun checkTimeSlot() {
        if (dataVenue.clubData.isclock == 0 && dataVenue.clubData.timeslot == 0) {
            binding.timeRecycler.visibility = View.GONE
            binding.timelayout.visibility = View.GONE
        } else if (dataVenue.clubData.isclock == 1) {
            binding.timeRecycler.visibility = View.GONE
            binding.timelayout.visibility = View.VISIBLE
        } else {
            callGettimeslot(selectedDate)
            binding.timeRecycler.visibility = View.VISIBLE
            binding.timelayout.visibility = View.GONE
        }
    }

    private fun callGettimeslot(selectedDate: String) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", dataVenue.clubData.clubId)
        input.addProperty("date", selectedDate)
        calendarviewpresenterimplementation!!.callGetTimeSlot(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    private fun setupAdapter() {
        binding.timeRecycler.layoutManager = GridLayoutManager(this, 3)
        adapter = TimeAdapter(this, timedata)
        binding.timeRecycler.adapter = adapter

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(applicationContext, MenuActivity::class.java)
            intent.putExtra(ConstantLib.VENUE_ID, getIntent().getStringExtra(ConstantLib.VENUE_ID))
            intent.putExtra(
                ConstantLib.SELECTED_VENUE_DIN_TOGO,
                getIntent().getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
            )
            intent.putExtra(ConstantLib.DATE, selectedDate)

            seletctedTime = "" + binding.timepicker.hour + ":" + binding.timepicker.minute + ":00"

            intent.putExtra(
                ConstantLib.TIME,
                seletctedTime
            )
            startActivity(intent)

        }




        binding.calendarView.setOnDayClickListener { eventDay ->

            val day = eventDay.calendar.get(Calendar.DAY_OF_MONTH).toString()
            val month = (eventDay.calendar.get(Calendar.MONTH) + 1).toString()
            val year = eventDay.calendar.get(Calendar.YEAR).toString()

            selectedDate = "" + eventDay.calendar.get(Calendar.YEAR) + "-" + eventDay.calendar.get(
                Calendar.MONTH
            ) + "-" + eventDay.calendar.get(Calendar.DAY_OF_MONTH)

            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.YEAR, eventDay.calendar.get(Calendar.YEAR))
            cal.set(Calendar.MONTH, eventDay.calendar.get(Calendar.MONTH))
            cal.set(Calendar.DAY_OF_MONTH, eventDay.calendar.get(Calendar.DAY_OF_MONTH))
            binding.calendarView.setDate(cal)
            checkTimeSlot()

        }
    }

    private fun ShowAMPM(mhour: Int): String {
        var hour = mhour
        var am_pm = ""
        if (hour > 12) {
            am_pm = "PM";
            hour -= 12;
        } else {
            am_pm = "AM";
        }
        return am_pm
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

    override fun onTimeSlotSuccess(responseData: TimeSlotResponse?) {
        timedata.clear()
        adapter.notifyDataSetChanged()
        timedata.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }

    override fun onTimeSlotFailure(responseData: String) {
        TODO("Not yet implemented")
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
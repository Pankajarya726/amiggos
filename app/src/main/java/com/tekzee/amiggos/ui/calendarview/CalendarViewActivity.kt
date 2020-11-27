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
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.Errortoast
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
        setupCalendar()
        setupClickListener()
        setupAdapter()
        setupLangauge()

        dataVenue = intent.getSerializableExtra(ConstantLib.CALENDAR_DATA) as VenueDetails.Data
        binding.timepicker.setIs24HourView(false)
        checkTimeSlot()


//        for (items in dataVenue.clubData.workingDays)
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

    private fun setupCalendar() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        binding.calendarView.setMinimumDate(cal)

        val calTodayDate = Calendar.getInstance()
        val day = calTodayDate.get(Calendar.DAY_OF_MONTH).toString()
        val month = (calTodayDate.get(Calendar.MONTH) + 1).toString()
        val year = calTodayDate.get(Calendar.YEAR).toString()

        selectedDate = "$year-$month-$day"
    }

    private fun setupLangauge() {
        binding.timeTitle.setText(languageData!!.time)
        binding.timeSlot.setText(languageData!!.time)
        binding.dateTitle.setText(languageData!!.date)
    }

    private fun checkTimeSlot() {
        if (dataVenue.clubData.isclock == 0 && dataVenue.clubData.timeslot == 0) {
            binding.timeslotlayout.visibility = View.GONE
            binding.timelayout.visibility = View.GONE
        } else if (dataVenue.clubData.isclock == 1) {
            binding.timeslotlayout.visibility = View.GONE
            binding.timelayout.visibility = View.VISIBLE
        } else {
            callGettimeslot(selectedDate)
            binding.timeslotlayout.visibility = View.VISIBLE
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

            if (dataVenue.clubData.isclock == 0 && dataVenue.clubData.timeslot == 0) {
                val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                menuIntent.putExtra(
                    ConstantLib.VENUE_ID,
                    intent.getStringExtra(ConstantLib.VENUE_ID)
                )
                menuIntent.putExtra(
                    ConstantLib.SELECTED_VENUE_DIN_TOGO,
                    intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
                )
                menuIntent.putExtra(ConstantLib.DATE, selectedDate)
                menuIntent.putExtra(
                    ConstantLib.TIME,
                    ""
                )
                startActivity(menuIntent)
            } else if (dataVenue.clubData.isclock == 1) {
                val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                menuIntent.putExtra(
                    ConstantLib.VENUE_ID,
                    intent.getStringExtra(ConstantLib.VENUE_ID)
                )
                menuIntent.putExtra(
                    ConstantLib.SELECTED_VENUE_DIN_TOGO,
                    intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
                )
                menuIntent.putExtra(ConstantLib.DATE, selectedDate)
                seletctedTime =
                    "" + binding.timepicker.hour + ":" + binding.timepicker.minute + ":00"
                menuIntent.putExtra(
                    ConstantLib.TIME,
                    seletctedTime
                )
                startActivity(menuIntent)
            } else if (dataVenue.clubData.timeslot == 1) {
                if (adapter.selected != null) {
                    val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                    menuIntent.putExtra(
                        ConstantLib.VENUE_ID,
                        intent.getStringExtra(ConstantLib.VENUE_ID)
                    )
                    menuIntent.putExtra(
                        ConstantLib.SELECTED_VENUE_DIN_TOGO,
                        intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
                    )
                    menuIntent.putExtra(ConstantLib.DATE, selectedDate)

                    menuIntent.putExtra(
                        ConstantLib.TIME,
                        adapter.selected
                    )
                    startActivity(menuIntent)
                } else {
                    binding.timeRecycler.requestFocus()
                    Errortoast("Please select any slot or select different date")
                }

            }
        }

        binding.calendarView.setOnDayClickListener { eventDay ->

            val day = eventDay.calendar.get(Calendar.DAY_OF_MONTH).toString()
            val month = (eventDay.calendar.get(Calendar.MONTH) + 1).toString()
            val year = eventDay.calendar.get(Calendar.YEAR).toString()
            selectedDate = "$year-$month-$day"

            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.YEAR, eventDay.calendar.get(Calendar.YEAR))
            cal.set(Calendar.MONTH, eventDay.calendar.get(Calendar.MONTH))
            cal.set(Calendar.DAY_OF_MONTH, eventDay.calendar.get(Calendar.DAY_OF_MONTH))
            binding.calendarView.setDate(cal)
            checkTimeSlot()

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTimeSlotSuccess(responseData: TimeSlotResponse?) {

        Coroutines.main {
            binding.scrollview.fullScroll(View.FOCUS_DOWN)
        }


        binding.timeslotlayout.visibility = View.VISIBLE
        timedata.clear()
        adapter.notifyDataSetChanged()
        timedata.addAll(responseData!!.data.timeSlot)
        adapter.notifyDataSetChanged()
    }

    override fun onTimeSlotFailure(message: String) {

        Coroutines.main {
            binding.scrollview.fullScroll(View.FOCUS_DOWN)
        }
        timedata.clear()
        adapter.notifyDataSetChanged()
        Errortoast(message)
        binding.timeslotlayout.visibility = View.GONE
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}
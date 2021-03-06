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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.applandeo.materialcalendarview.EventDay
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.ActivityCalendarBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.ui.calendarview.adapter.TimeAdapter
import com.tekzee.amiggos.ui.calendarview.model.TimeSlotResponse
import com.tekzee.amiggos.ui.choosepackage.ChoosePackageActivity
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

    private var meventDay: EventDay?=null
    private lateinit var dataVenue: VenueDetails.Data
    private lateinit var adapter: TimeAdapter
    var fmt: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    //    private lateinit var data: ChooseWeekResponse
    private lateinit var binding: ActivityCalendarBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    val disabledDates = ArrayList<Calendar>()
    var timedata = ArrayList<String>()
    var selectedDate = ""
    var seletctedTime = ""
    private var calendarviewpresenterimplementation: CalendarViewPresenterImplementation? = null
    private var repository: ItemRepository? = null
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
        setupRepository()
        dataVenue = intent.getSerializableExtra(ConstantLib.CALENDAR_DATA) as VenueDetails.Data
        binding.timepicker.setIs24HourView(false)


        checkTimeSlot()
        val day: Calendar = Calendar.getInstance()
        for (i in 1..365) {



            if (dataVenue.clubData.workingDays[0].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }
            if (dataVenue.clubData.workingDays[1].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }
            if (dataVenue.clubData.workingDays[2].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }
            if (dataVenue.clubData.workingDays[3].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }
            if (dataVenue.clubData.workingDays[4].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }

            if (dataVenue.clubData.workingDays[5].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }

            if (dataVenue.clubData.workingDays[6].isOpen == 0) {
                if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    val d = day.clone()
                    disabledDates.add(d as Calendar)
                }
            }

            day.add(Calendar.DATE, 1)
        }
        binding.calendarView.setDisabledDays(disabledDates)
    }
    private fun setupRepository() {
        val itemDao = AmiggoRoomDatabase.getDatabase(this).itemDao()
        repository = ItemRepository(itemDao)
    }

    private fun setupCalendar() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, -1)
        binding.calendarView.setMinimumDate(cal)

        val calTodayDate = Calendar.getInstance()
        val day = calTodayDate.get(Calendar.DAY_OF_MONTH).toString()
        val month = (calTodayDate.get(Calendar.MONTH) + 1).toString()
        val year = calTodayDate.get(Calendar.YEAR).toString()

        meventDay = EventDay(Calendar.getInstance())
        selectedDate = "$year-$month-$day"
        selectedDate = fmt.format(meventDay!!.calendar.time).toString()
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

            val cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,-1)
            if (!checkDisabledDate(meventDay!!.calendar) && meventDay!!.calendar.after(cal)) {
                if (dataVenue.clubData.isclock == 0 && dataVenue.clubData.timeslot == 0) {
                    Coroutines.main {
                        repository!!.clearCart()
                    }
                    val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                    menuIntent.putExtra(
                        ConstantLib.VENUE_ID,
                        intent.getStringExtra(ConstantLib.VENUE_ID)
                    )
                    menuIntent.putExtra(
                        ConstantLib.SELECTED_VENUE_DIN_TOGO,
                        intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
                    )
                    menuIntent.putExtra(
                        ConstantLib.FROM,
                        ConstantLib.MENUACTIVITY
                    )
                    menuIntent.putExtra(ConstantLib.DATE, selectedDate)
                    menuIntent.putExtra(
                        ConstantLib.TIME,
                        ""
                    )
                    startActivity(menuIntent)
                } else if (dataVenue.clubData.isclock == 1) {
                    Coroutines.main {
                        repository!!.clearCart()
                    }
                    val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                    menuIntent.putExtra(
                        ConstantLib.FROM,
                        ConstantLib.MENUACTIVITY
                    )
                    menuIntent.putExtra(
                        ConstantLib.VENUE_ID,
                        intent.getStringExtra(ConstantLib.VENUE_ID)
                    )
                    menuIntent.putExtra(
                        ConstantLib.SELECTED_VENUE_DIN_TOGO,
                        intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
                    )
                    menuIntent.putExtra(ConstantLib.DATE, selectedDate)

                    var sminute: Int = binding.timepicker.minute
                    var selectedMinute =""
                    if(sminute.toString().length==1){
                        selectedMinute = "0"+sminute.toString()
                    }   else{
                        selectedMinute = sminute.toString()
                    }
                    seletctedTime =
                        "" + binding.timepicker.hour + ":" + selectedMinute + ":00"
                    menuIntent.putExtra(
                        ConstantLib.TIME,
                        seletctedTime
                    )
                    startActivity(menuIntent)
                } else if (dataVenue.clubData.timeslot == 1) {
                    Coroutines.main {
                        repository!!.clearCart()
                    }
                    if (adapter.selected != null) {
                        val menuIntent = Intent(applicationContext, MenuActivity::class.java)
                        menuIntent.putExtra(
                            ConstantLib.FROM,
                            ConstantLib.MENUACTIVITY
                        )
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
                        Utility.showErrorDialog(this,languageData!!.bookingunavailable)
                    }

                }
            }else{
                val pDialog =
                    SweetAlertDialog(this@CalendarViewActivity, SweetAlertDialog.ERROR_TYPE)
                pDialog.titleText = languageData!!.bookingunavailable
                pDialog.setCancelable(false)
                pDialog.setConfirmButton(languageData!!.klDismiss) {
                    pDialog.dismiss()
                }
                pDialog.show()
            }

        }


        binding.calendarView.setOnDayClickListener { eventDay: EventDay ->

            Log.e("meventdat---",""+eventDay!!.calendar.get(Calendar.DAY_OF_WEEK))

            val day = eventDay.calendar.get(Calendar.DAY_OF_MONTH).toString()
            val month = (eventDay.calendar.get(Calendar.MONTH) + 1).toString()
            val year = eventDay.calendar.get(Calendar.YEAR).toString()
            val checkDate = Calendar.getInstance()
            checkDate.add(Calendar.DAY_OF_MONTH,-1)
            if(!eventDay.calendar.after(checkDate)){
                val pDialog =
                    SweetAlertDialog(this@CalendarViewActivity, SweetAlertDialog.ERROR_TYPE)
                pDialog.titleText = languageData!!.bookingunavailable
                pDialog.setCancelable(false)
                pDialog.setConfirmButton(languageData!!.klDismiss) {
                    pDialog.dismiss()
                }
                pDialog.show()
                return@setOnDayClickListener
            }
            meventDay = eventDay
//            selectedDate = "$year-$month-$day"
            selectedDate = fmt.format(eventDay!!.calendar.time).toString()
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.YEAR, eventDay.calendar.get(Calendar.YEAR))
            cal.set(Calendar.MONTH, eventDay.calendar.get(Calendar.MONTH))
            cal.set(Calendar.DAY_OF_MONTH, eventDay.calendar.get(Calendar.DAY_OF_MONTH))

            binding.calendarView.setDate(cal)


            if (!checkDisabledDate(eventDay.calendar)) {
                checkTimeSlot()
            } else {
                val pDialog =
                    SweetAlertDialog(this@CalendarViewActivity, SweetAlertDialog.ERROR_TYPE)
                pDialog.titleText = languageData!!.bookingunavailable
                pDialog.setCancelable(false)
                pDialog.setConfirmButton(languageData!!.klDismiss) {
                    pDialog.dismiss()
                }
                pDialog.show()
            }


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
        binding.timeslotlayout.visibility = View.GONE

        Utility.showErrorDialog(this,message)
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    private fun checkDisabledDate(eventDay: Calendar): Boolean {
        var flag: Boolean = false
        for (items in disabledDates) {
            val itemeventdate =
                items.get(Calendar.DAY_OF_MONTH).toString()+ items.get(Calendar.MONTH).toString() + items.get(Calendar.YEAR).toString()
            val meventdaydate =
                eventDay.get(Calendar.DAY_OF_MONTH).toString() + eventDay.get(Calendar.MONTH).toString() + eventDay.get(Calendar.YEAR).toString()

            if (itemeventdate.equals(meventdaydate,false)) {
                flag = true
            }
        }
        return flag

    }
}
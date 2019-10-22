package com.tekzee.amiggos.ui.mybooking

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.BookingActivityBinding
import com.tekzee.amiggos.ui.mybooking.adapter.MyBookingAdapter
import com.tekzee.amiggos.ui.mybooking.model.MyBookingData
import com.tekzee.amiggos.ui.mybooking.model.MyBookingResponse
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import androidx.recyclerview.widget.DividerItemDecoration
import com.tekzee.amiggos.ui.bookingdetail.BookingDetailsActivity


class MyBookingActivity : BaseActivity(), MyBookingPresenter.MyBookingMainView {

    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: MyBookingAdapter? = null

    private lateinit var binding: BookingActivityBinding
    private var myBookingPresenterImplementation: MyBookingPresenterImplementation? = null
    private var data = ArrayList<MyBookingData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.booking_activity)
        myBookingPresenterImplementation = MyBookingPresenterImplementation(this, this)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        callMyBookingApi()
        setupRecyclerView()
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klMyBooking
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecyclerView() {
        binding.bookingRecyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        binding.bookingRecyclerview.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.bookingRecyclerview.context,
            layoutManager.orientation
        )
        binding.bookingRecyclerview.addItemDecoration(dividerItemDecoration)
        adapter = MyBookingAdapter(data,object : BookingClicked {
            override fun onBookingClicked(position: Int, selectedData: MyBookingData) {
                val intent = Intent(applicationContext, BookingDetailsActivity::class.java)
                intent.putExtra(ConstantLib.USER_DATA,selectedData)
                startActivity(intent)
            }

        })
        binding.bookingRecyclerview.adapter = adapter



    }


    private fun callMyBookingApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        myBookingPresenterImplementation!!.doMyBookingApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }


    override fun onMyBookingSuccess(responseData: MyBookingResponse?) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter!!.notifyDataSetChanged()
    }

}
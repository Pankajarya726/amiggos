package com.tekzee.amiggoss.ui.mybooking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.BookingActivityBinding
import com.tekzee.amiggoss.ui.mybooking.adapter.MyBookingAdapter
import com.tekzee.amiggoss.ui.mybooking.model.MyBookingData
import com.tekzee.amiggoss.ui.mybooking.model.MyBookingResponse
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import androidx.recyclerview.widget.DividerItemDecoration
import com.tekzee.amiggoss.ui.bookingqrcode.GetBookingQrCodeActivity
import com.tekzee.mallortaxi.base.BaseFragment


class MyBookingActivity : BaseFragment(), MyBookingPresenter.MyBookingMainView {

    private var languageData: LanguageData? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: MyBookingAdapter? = null

    private lateinit var binding: BookingActivityBinding
    private var myBookingPresenterImplementation: MyBookingPresenterImplementation? = null
    private var data = ArrayList<MyBookingData>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =DataBindingUtil.inflate(
            inflater, R.layout.booking_activity, container, false);
        myBookingPresenterImplementation = MyBookingPresenterImplementation(this, activity!!)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        callMyBookingApi()

        setupRecyclerView()
        return binding.root
    }




    private fun setupRecyclerView() {
        binding.bookingRecyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding.bookingRecyclerview.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.bookingRecyclerview.context,
            layoutManager.orientation
        )
        binding.bookingRecyclerview.addItemDecoration(dividerItemDecoration)
        adapter = MyBookingAdapter(data,object : BookingClicked {
            override fun onBookingClicked(position: Int, selectedData: MyBookingData) {
                val intent = Intent(activity, GetBookingQrCodeActivity::class.java)
                intent.putExtra(ConstantLib.BOOKING_ID,selectedData.id.toString())
                intent.putExtra(ConstantLib.FROM,"Booking")
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
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onMyBookingSuccess(responseData: MyBookingResponse?) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData!!.data)
        adapter!!.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        myBookingPresenterImplementation!!.onStop()
    }

}
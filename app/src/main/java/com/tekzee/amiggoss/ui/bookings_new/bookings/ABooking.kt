package com.tekzee.amiggoss.ui.bookings_new.bookings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.ABookingFragmentBinding
import com.tekzee.amiggoss.ui.bookingdetails.ABookingDetails
import com.tekzee.amiggoss.ui.bookings_new.bookings.adapter.BookingAdapter
import com.tekzee.amiggoss.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.constant.ConstantLib
import com.tekzee.amiggoss.ui.homescreen_new.NotifyNotification
import java.util.*

class ABooking : BaseFragment(), ABookingPresenter.ABookingPresenterMainView
{

    private lateinit var notifylistner: NotifyNotification
    private val data: ArrayList<ABookingResponse.Data.BookingData> = ArrayList()
    private lateinit var binding: ABookingFragmentBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aBookingPresenterImplementation: ABookingPresenterImplementation? = null
    private var adapter: BookingAdapter?=null

    companion object {

        fun newInstance(): ABooking {
            return ABooking()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.a_booking_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aBookingPresenterImplementation = ABookingPresenterImplementation(this,context!!)
        sharedPreference = SharedPreference(context!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupVenueTaggedRecycler()
        callGetBookings()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.error.errorLayout.setOnClickListener {
            data.clear()
            adapter!!.notifyDataSetChanged()
            callGetBookings()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        notifylistner = activity as NotifyNotification
    }


    private fun setupVenueTaggedRecycler() {
        binding.aBookingRecyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding.aBookingRecyclerview.layoutManager = layoutManager
        adapter = BookingAdapter(data, object : BookingClickedListener {
            override fun onBookingClicked(bookingData: ABookingResponse.Data.BookingData) {
                val intent = Intent(activity,ABookingDetails::class.java)
                intent.putExtra(ConstantLib.BOOKING_DATA,bookingData)
                context!!.startActivity(intent)
                Animatoo.animateSlideRight(activity)
            }
        })
        binding.aBookingRecyclerview.adapter = adapter
    }

    private fun callGetBookings() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        aBookingPresenterImplementation!!.docallGetBookings(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }



    override fun onBookingSuccess(taggedVenue: List<ABookingResponse.Data.BookingData>) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(taggedVenue)
        adapter!!.notifyDataSetChanged()
        setupErrorVisibility()
    }

    override fun onBookingFailure(message: String) {
        setupErrorVisibility()
    }


    override fun validateError(message: String) {
        setupErrorVisibility()
    }

    override fun onStop() {
        super.onStop()
        aBookingPresenterImplementation!!.onStop()
    }



    fun setupErrorVisibility(){
        if(data.size == 0){
            binding.error.errorLayout.visibility = View.VISIBLE
            binding.aBookingRecyclerview.visibility = View.GONE
        }else{
            binding.aBookingRecyclerview.visibility = View.VISIBLE
            binding.error.errorLayout.visibility = View.GONE
        }
    }

}
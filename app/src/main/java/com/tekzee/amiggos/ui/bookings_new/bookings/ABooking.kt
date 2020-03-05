package com.tekzee.amiggos.ui.bookings_new.bookings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.ABookingFragmentBinding
import com.tekzee.amiggos.ui.bookingdetails.ABookingDetails
import com.tekzee.amiggos.ui.bookings_new.bookings.adapter.BookingAdapter
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.util.*

class ABooking : BaseFragment(), ABookingPresenter.ABookingPresenterMainView
{

    private val data: ArrayList<ABookingResponse.Data.UpcomingParty> = ArrayList()
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
        aBookingPresenterImplementation = ABookingPresenterImplementation(this,activity!!)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupVenueTaggedRecycler()
        callGetBookings()
        return binding.root
    }

    private fun setupVenueTaggedRecycler() {
        binding.aBookingRecyclerview.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding.aBookingRecyclerview.layoutManager = layoutManager
        adapter = BookingAdapter(data, object : BookingClickedListener {
            override fun onBookingClicked(upcomingParty: ABookingResponse.Data.UpcomingParty) {
                val intent = Intent(activity,ABookingDetails::class.java)
                intent.putExtra(ConstantLib.BOOKING_DATA,upcomingParty)
                activity!!.startActivity(intent)
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



    override fun onBookingSuccess(taggedVenue: List<ABookingResponse.Data.UpcomingParty>) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(taggedVenue)
        adapter!!.notifyDataSetChanged()
    }

    override fun onBookingFailure(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        aBookingPresenterImplementation!!.onStop()
    }

}
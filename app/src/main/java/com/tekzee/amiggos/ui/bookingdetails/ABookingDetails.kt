package com.tekzee.amiggos.ui.bookingdetails

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.databinding.ABookingDetailsBinding
import com.tekzee.amiggos.ui.bookings_new.bookings.model.ABookingResponse
import com.tekzee.mallortaxiclient.constant.ConstantLib

class ABookingDetails: BaseActivity() {

    private var dataFromIntent: ABookingResponse.Data.UpcomingParty? =null
    var binding: ABookingDetailsBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_booking_details)
        dataFromIntent =intent.getSerializableExtra(ConstantLib.BOOKING_DATA) as ABookingResponse.Data.UpcomingParty
        setupUi()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupUi() {
        Glide.with(applicationContext).load(dataFromIntent!!.venueHomeImage).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        binding!!.txtName.text = dataFromIntent!!.clubName
        binding!!.txtLocation.text = dataFromIntent!!.partyDate
        binding!!.bookingid.text = "Booking Id : "+dataFromIntent!!.bookingId
        binding!!.date.text = "Date : "+dataFromIntent!!.partyDate
        binding!!.starttime.text = "Start Time : "+dataFromIntent!!.startTime
        binding!!.endtime.text = "End Time : "+dataFromIntent!!.endTime
        binding!!.address.text = "Address : "+dataFromIntent!!.clubAddress
    }


    override fun validateError(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideLeft(this)
    }
}
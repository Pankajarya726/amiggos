package com.tekzee.amiggos.ui.bookingdetail

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.BookingDetailsBinding
import com.tekzee.amiggos.ui.mybooking.model.MyBookingData
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib

class BookingDetailsActivity: BaseActivity() {

    private lateinit var binding:BookingDetailsBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    override fun validateError(message: String) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.booking_details)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupToolBar()
        setupView(intent.getSerializableExtra(ConstantLib.USER_DATA) as MyBookingData)
    }


    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.qr_code_screen_title
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }



    private fun setupView(myBookingData: MyBookingData) {
        Glide.with(applicationContext).load(myBookingData.qrCode).into(binding.dImgQrCode)
        binding.txtBookingDetails.setText(languageData!!.qr_code_screen_title)
        binding.txtQrCode.setText(myBookingData.bookingCode)
        binding.txtPartyName.setText(myBookingData.clubName)
        binding.txtDate.setText("Date : ")
        binding.txtAmount.setText("Paid : ")
        binding.dTxtDate.setText(myBookingData.dateTime)
        binding.dTxtAmount.setText(myBookingData.symbolLeft+" "+myBookingData.price)
    }
}
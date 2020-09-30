package com.tekzee.amiggos.ui.bookingqrcode

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.BookingQrcodeActivityBinding
import com.tekzee.amiggos.ui.bookingqrcode.model.BookinQrCodeResponse
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class GetBookingQrCodeActivity: BaseActivity(), BookingQrCodePresenter.BookingQrCodeMainView {

    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var bookingQrCodePresenterImplementation: BookingQrCodePresenterImplementation? = null

    private lateinit var binding: BookingQrcodeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.booking_qrcode_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        bookingQrCodePresenterImplementation = BookingQrCodePresenterImplementation(this,this)
        setupToolBar()
        setupViewData()
        callGetBookingApi()

    }

    private fun callGetBookingApi() {
        val input: JsonObject = JsonObject()
        Logger.d("bookingid---->"+intent.getStringExtra(ConstantLib.BOOKING_ID))
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        bookingQrCodePresenterImplementation!!.doGetBookingQrCode(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun setupViewData() {
        binding.txtTitle.text = languageData!!.klLblBookingDetailsTitle
        binding.txtDescription.text = languageData!!.klLblBookingDetailsTitle
        binding.txtCode.text = languageData!!.qrCodeScreenBottomText

    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.title.text = languageData!!.klVenues
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


    override fun onGetBookingQrCodeSuccess(responseData: BookinQrCodeResponse?) {
        binding.txtDescription.text = responseData!!.data.subTitle
        binding.txtTitle.text = responseData.data.title
        binding.txtCode.text = responseData.data.bottomText
        binding.btnQrcode.text = responseData.data.bookingCode
        Glide.with(applicationContext).load(responseData.data.qrCode).into(binding.imgQrcode)

    }

    override fun onBackPressed() {

        if(intent.getStringExtra(ConstantLib.FROM).equals("Booking",true)){
            super.onBackPressed()
        }else{
            val intent = Intent(applicationContext,AHomeScreen::class.java)
            startActivity(intent)
            finishAffinity()
        }

    }



}
package com.tekzee.amiggos.ui.bookingdetailnew

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.BookingdetailnewActivityBinding
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import com.tekzee.amiggos.ui.friendlist.model.FriendListResponse
import com.tekzee.amiggos.ui.invitefriendnew.InviteFriendNewActivity
import com.tekzee.amiggos.ui.viewmenu.ViewMenuActivity
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility

class BookingDetailNewActivity: BaseActivity(),
    BookingDetailsNewPresenter.ABookingDetailsPresenterMainView {

    private var bookingData: BookingDetailsNewResponse? = null
    private lateinit var bookingdetailnewpresenterimplementation: BookingDetailNewPresenterImplementation
    private lateinit var binding: BookingdetailnewActivityBinding
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.bookingdetailnew_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        bookingdetailnewpresenterimplementation = BookingDetailNewPresenterImplementation(this, this)
        getBookingDetailsApi()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.imgClose.setOnClickListener {
            onBackPressed()
        }

        binding.viewmenu.setOnClickListener {
            val intent = Intent(this, ViewMenuActivity::class.java)
            intent.putExtra(ConstantLib.VIEWMENUDATA,bookingData!!.data.booking)
            startActivity(intent)
        }

        binding.inviteFriend.setOnClickListener {
            val intent = Intent(this, InviteFriendNewActivity::class.java)
            intent.putExtra(ConstantLib.MESSAGE,"")
            intent.putExtra(ConstantLib.FROM,ConstantLib.BOOKINGDETAILPAGE)
            intent.putExtra(ConstantLib.BOOKING_ID,bookingData!!.data.booking.bookingId.toString())
            startActivity(intent)

        }
    }

    private fun getBookingDetailsApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", intent.getStringExtra(ConstantLib.BOOKING_ID))
        bookingdetailnewpresenterimplementation.dogetBookingDetails(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onFriendListSuccess(responseData: FriendListResponse?) {
        TODO("Not yet implemented")
    }

    override fun onBookingDetailSuccess(responseData: BookingDetailsNewResponse?) {
        binding.cardLayout.visibility = View.VISIBLE
        setupViewData(responseData)

    }

    override fun onBookingDetailFailure(responseData: String) {
        binding.cardLayout.visibility = View.GONE
        finish()
    }

    private fun setupViewData(responseData: BookingDetailsNewResponse?) {
        bookingData = responseData
        binding.venueName.text = responseData!!.data.booking.name
        binding.venueAddress.text = responseData.data.booking.address
        Glide.with(this).load(responseData.data.booking.venueHomeImage).into(binding.imgVenue)
        binding.txtTime.text = responseData.data.booking.bookingTime
        binding.txtDate.text = responseData.data.booking.bookingDate
        if(responseData.data.booking.totalInvitedGuest>0){
            binding.txtGuestList.visibility = View.VISIBLE
        }else{
            binding.txtGuestList.visibility = View.GONE
        }
        binding.txtGuestList.text = languageData!!.klGuestList+responseData.data.booking.totalInvitedGuest.toString()
        binding.txtPurchaseDescriptionTitle.text = languageData!!.purchasedescription
        binding.txtPurchaseDescription.text = Html.fromHtml(responseData.data.booking.description)
        binding.txtPurchaseAmount.text = "$ "+responseData.data.booking.totalAmount.toString()
        binding.txtTicketinfo.text = responseData.data.booking.totalAmount.toString()
        binding.txtReferenceNo.text = languageData!!.referencenumber+responseData.data.booking.bookingId.toString()
        binding.txtPuchasedBy.text = languageData!!.purchasedby+responseData.data.booking.purchasedBy
        Glide.with(this).load(responseData.data.booking.qrCode).into(binding.imgBarcode)
        if(responseData.data.booking.menus.isNotEmpty()){
           binding.viewmenu.visibility= View.VISIBLE
        }else{
            binding.viewmenu.visibility= View.GONE
        }
    }

    override fun onFriendInviteSuccess(responseData: CommonResponse?) {
        TODO("Not yet implemented")
    }

    override fun validateError(message: String) {
        Errortoast(message)
    }

}
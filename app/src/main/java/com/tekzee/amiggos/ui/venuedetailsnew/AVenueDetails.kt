package com.tekzee.amiggos.ui.venuedetailsnew

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.gson.JsonObject
import com.impulsiveweb.galleryview.GalleryView
import com.like.LikeButton
import com.like.OnLikeListener
import com.smarteist.autoimageslider.SliderAnimations
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomSheetFragment
import com.tekzee.amiggos.databinding.AVenueDetailsBinding
import com.tekzee.amiggos.ui.calendarview.CalendarViewActivity
import com.tekzee.amiggos.ui.profiledetails.SliderClickListener
import com.tekzee.amiggos.ui.profiledetails.model.SliderAdapterExample
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import java.lang.StringBuilder
import java.util.*


class AVenueDetails : BaseActivity(), AVenueDetailsPresenter.AVenueDetailsPresenterMainView,
    SliderClickListener {
    private lateinit var phoneNumberForCall: String
    private var isBookingAvailable: Int = 0
    private var response: VenueDetails.Data? = null
    private var venueId: String? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aVenueDetailsPresenterImplementation: AVenueDetailsPresenterImplementation? = null
    private var binding: AVenueDetailsBinding? = null
    private lateinit var adapter: SliderAdapterExample
    private lateinit var list: java.util.ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_venue_details)
        adapter = SliderAdapterExample(this, this)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aVenueDetailsPresenterImplementation = AVenueDetailsPresenterImplementation(this, this)
        venueId = intent.getStringExtra(ConstantLib.VENUE_ID)
        setupClickListener()
        callVenueDetailsApi()
        setupLangauge()
        setupViewPager()
    }


    private fun setupViewPager() {
        binding!!.htabHeader.sliderAdapter = adapter
        //binding!!.htabHeader.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding!!.htabHeader.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding!!.htabHeader.indicatorSelectedColor = Color.WHITE
        binding!!.htabHeader.indicatorUnselectedColor = Color.GRAY


    }


    private fun setupLangauge() {
        binding!!.venueDetail.text = languageData!!.klVenuedetails
        binding!!.address.text = languageData!!.address
        binding!!.phonenumber.text = languageData!!.phonenumber
        binding!!.googleVenue.text = languageData!!.google_venue_warning
    }

    private fun callVenueDetailsApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", venueId)
        aVenueDetailsPresenterImplementation!!.callVenueDetailsApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun setupClickListener() {

        binding!!.txtPhone.setOnClickListener {
            askPermission(Manifest.permission.CALL_PHONE) {

                val pDialog = SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                pDialog.titleText = languageData!!.callmessage +phoneNumberForCall+"?"
                pDialog.setCancelable(false)
                pDialog.setCancelButton(languageData!!.klCancel) {
                    pDialog.dismiss()
                }
                pDialog.setConfirmButton(languageData!!.klOk) {
                    pDialog.dismiss()
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumberForCall))
                    startActivity(intent)
                }
                pDialog.show()

            }.onDeclined { e ->
                if (e.hasDenied()) {
                    AlertDialog.Builder(this)
                        .setMessage(languageData!!.callpermission)
                        .setPositiveButton(languageData!!.yes) { dialog, which ->
                            e.askAgain()
                        } //ask again
                        .setNegativeButton(languageData!!.no) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {
                    e.goToSettings()
                }
            }


        }

        binding!!.navigation.setOnClickListener {
            val uri = java.lang.String.format(
                Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=" + sharedPreference!!.getValueString(ConstantLib.CURRENTLAT) + "," + sharedPreference!!.getValueString(
                    ConstantLib.CURRENTLNG
                ) + "&daddr=" + response!!.clubData.latitude + "," + response!!.clubData.longitude,
                12f,
                2f,
            )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding!!.txtAddress.setOnClickListener {
            val uri = java.lang.String.format(
                Locale.ENGLISH,
                "http://maps.google.com/maps?saddr=" + sharedPreference!!.getValueString(ConstantLib.CURRENTLAT) + "," + sharedPreference!!.getValueString(
                    ConstantLib.CURRENTLNG
                ) + "&daddr=" + response!!.clubData.latitude + "," + response!!.clubData.longitude,
                12f,
                2f,
            )
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                try {
                    val unrestrictedIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(unrestrictedIntent)
                } catch (innerEx: ActivityNotFoundException) {
                    Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }



        binding!!.bookNow.setOnClickListener {
            if (isBookingAvailable == 1) {
                if (response!!.clubData.goOrder == 1 && response!!.clubData.reservation == 1) {
                    showCustomDialog(binding!!.bookNow, response!!)
                } else if (response!!.clubData.goOrder == 1) {
                    sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO, ConstantLib.TOGO)
                    val intent = Intent(applicationContext, CalendarViewActivity::class.java)
                    intent.putExtra(ConstantLib.CALENDAR_DATA, response!!)
                    intent.putExtra(ConstantLib.VENUE_ID, response!!.clubData.clubId.toString())
                    intent.putExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO, "To-Go")
                    startActivity(intent)
                } else if (response!!.clubData.reservation == 1) {
                    sharedPreference!!.save(
                        ConstantLib.SELECTED_VENUE_DIN_TOGO,
                        ConstantLib.RESERVATION
                    )
                    val intent = Intent(applicationContext, CalendarViewActivity::class.java)
                    intent.putExtra(ConstantLib.CALENDAR_DATA, response!!)
                    intent.putExtra(ConstantLib.VENUE_ID, response!!.clubData.clubId.toString())
                    intent.putExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO, "Dine-In")
                    startActivity(intent)
                }
            }
        }




        binding!!.imgHeart.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                callLikeUnlikeApi()
            }

            override fun unLiked(likeButton: LikeButton?) {
                callLikeUnlikeApi()
            }
        })
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        val intent = Intent()
        setResult(2, intent)
        finish()
        Animatoo.animateSlideLeft(this)
    }

    private fun callLikeUnlikeApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", venueId)
        aVenueDetailsPresenterImplementation!!.callLikeUnlikeApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    private fun setupViews(response: VenueDetails.Data) {

        if (intent.getIntExtra(ConstantLib.IS_GOOGLE_VENUE, 0) == 1) {
            binding!!.googleVenue.visibility = View.VISIBLE
            binding!!.imgHeart.visibility = View.GONE
            binding!!.amiggosVenue.visibility = View.GONE
            binding!!.bookNow.visibility = View.GONE
        } else {
            binding!!.googleVenue.visibility = View.GONE
            binding!!.amiggosVenue.visibility = View.VISIBLE
            binding!!.imgHeart.visibility = View.VISIBLE
            binding!!.bookNow.visibility = View.VISIBLE
        }

        binding!!.txtName.text = response.clubData.name
        binding!!.txtClubType.text = response.clubData.menuTypeName
        binding!!.txtLocation.text = response.clubData.clubCity + ", " + response.clubData.clubState
        binding!!.txtAgegroup.text = response.clubData.agelimit
        binding!!.imgHeart.isLiked = response.clubData.isFavorite == 1
        binding!!.txtDescription.text = response.clubData.clubDescription
        binding!!.txtAddress.paintFlags =
            binding!!.txtAddress.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding!!.txtAddress.text = response.clubData.address
        binding!!.txtPhone.text = response.clubData.phoneNumber
        phoneNumberForCall = response.clubData.phoneNumber
        isBookingAvailable = response.clubData.isBookingAvailable.toInt()
        if (response.clubData.isBookingAvailable.toInt() == 1) {
            binding!!.bookNow.text = languageData!!.booknow
        } else {
            binding!!.bookNow.text = languageData!!.not_currenty_booking
        }



        if (response.clubData.maskReq == 1) {
            binding!!.maskimage.visibility = View.VISIBLE
            Glide.with(this).load(response.clubData.maskimage).into(
                binding!!.maskimage
            )
        } else {
            binding!!.maskimage.visibility = View.GONE
        }

        val hoursOpen:StringBuilder = StringBuilder()
        for(item in response.clubData.workingDays){
            if(item.isOpen==1){
                 hoursOpen.append(item.name)
                 hoursOpen.append(" : ")
                 hoursOpen.append(item.timing)
                 hoursOpen.append(System.getProperty("line.separator"))
            }
        }

        binding!!.txtHours.text = hoursOpen
        binding!!.hours.text = languageData!!.operationHours


    }

    override fun onVenueDetailsSuccess(responseData: VenueDetails.Data) {
        response = responseData
        setupViews(response!!)
        list = ArrayList()
        list.addAll(responseData.clubData.homeImage)
        adapter.renewItems(list)
    }

    override fun onVenueDetailsFailure(message: String) {
        Errortoast(message)
        finish()
    }

    override fun onLikeUnlikeSuccess(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(applicationContext, languageData!!.session_error)
    }


    fun showCustomDialog(view: View, response: VenueDetails.Data) {
        val bottomSheetDialog: BottomSheetFragment =
            BottomSheetFragment.newInstance(this.response!!.clubData.clubId, response)
        bottomSheetDialog.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
    }

    override fun onSliderClicked(position: Int) {
        GalleryView.show(this, list, position)
    }

}
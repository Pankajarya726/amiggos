package com.tekzee.amiggos.ui.venuedetailsnew

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.impulsiveweb.galleryview.GalleryView
import com.like.LikeButton
import com.like.OnLikeListener
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomSheetFragment
import com.tekzee.amiggos.databinding.AVenueDetailsBinding
import com.tekzee.amiggos.ui.profiledetails.SliderClickListener
import com.tekzee.amiggos.ui.profiledetails.model.SliderAdapterExample
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubData
import com.tekzee.amiggos.ui.venuedetailsnew.model.VenueDetails
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import java.util.*


class AVenueDetails : BaseActivity(), AVenueDetailsPresenter.AVenueDetailsPresenterMainView,
    SliderClickListener {
    private var isBookingAvailable: Int=0
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
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.bookNow.setOnClickListener{
            if(isBookingAvailable ==1){
                if(response!!.clubData.goOrder ==1 && response!!.clubData.reservation ==1){
                    showCustomDialog(binding!!.bookNow, response!!)
                }else if(response!!.clubData.goOrder ==1){
                    sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO,ConstantLib.TOGO)
                }else if(response!!.clubData.reservation ==1){
                    sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO,ConstantLib.RESERVATION)
                }else{
                    sharedPreference!!.save(ConstantLib.SELECTED_VENUE_DIN_TOGO,"")
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

        if(intent.getIntExtra(ConstantLib.IS_GOOGLE_VENUE,0)==1){
            binding!!.googleVenue.visibility = View.VISIBLE
            binding!!.amiggosVenue.visibility = View.GONE
        }else{
            binding!!.googleVenue.visibility = View.GONE
            binding!!.amiggosVenue.visibility = View.VISIBLE
        }

        binding!!.txtName.text = response.clubData.name
        binding!!.txtClubType.text = response.clubData.menuTypeName
        binding!!.txtLocation.text = response.clubData.clubCity+", "+response.clubData.clubState
        binding!!.txtAgegroup.text = response.clubData.agelimit
        binding!!.imgHeart.isLiked = response.clubData.isFavorite==1
        binding!!.txtDescription.text = response.clubData.clubDescription
        binding!!.txtAddress.text = response.clubData.address
        binding!!.txtPhone.text = response.clubData.phoneNumber
        isBookingAvailable = response.clubData.isBookingAvailable
        if(response.clubData.isBookingAvailable == 1){
            binding!!.bookNow.setText(languageData!!.booknow)
        }else{
            binding!!.bookNow.setText(languageData!!.closed)
        }



        if(response.clubData.maskReq==1){
            binding!!.maskimage.visibility = View.VISIBLE
            Glide.with(this).load(response.clubData.maskimage).into(
                binding!!.maskimage
            )
        }else{
            binding!!.maskimage.visibility = View.GONE
        }

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




    fun showCustomDialog(view: View, response: VenueDetails.Data){
        val bottomSheetDialog: BottomSheetFragment = BottomSheetFragment.newInstance(this.response!!.clubData.clubId,response)
        bottomSheetDialog.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")
    }

    override fun onSliderClicked(position: Int) {
        GalleryView.show(this, list,position)
    }

}
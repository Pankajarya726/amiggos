package com.tekzee.amiggos.ui.venuedetailsnew

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.like.LikeButton
import com.like.OnLikeListener
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.AVenueDetailsBinding
import com.tekzee.amiggos.ui.venuedetails.VenueDetailsActivity
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubData
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib


class AVenueDetails : BaseActivity(), AVenueDetailsPresenter.AVenueDetailsPresenterMainView {
    private var response: ClubDetailResponse.Data?=null
    private var dataClub: ClubData? =null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aVenueDetailsPresenterImplementation: AVenueDetailsPresenterImplementation? = null
    private var binding: AVenueDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_venue_details)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aVenueDetailsPresenterImplementation = AVenueDetailsPresenterImplementation(this, this)
        dataClub = intent.getSerializableExtra(ConstantLib.VENUE_DATA) as ClubData
        setupClickListener()
        callVenueDetailsApi()

        setupViews()
    }

    private fun callVenueDetailsApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("club_id", dataClub!!.clubId)
        aVenueDetailsPresenterImplementation!!.callVenueDetailsApi(input, Utility.createHeaders(sharedPreference))
    }

    private fun setupClickListener() {
        binding!!.imgBack.setOnClickListener {
            onBackPressed()
        }

        binding!!.txtDetails.setOnClickListener{
            if(response!=null){
                val intentActivity = Intent(applicationContext, VenueDetailsActivity::class.java)
                intentActivity.putExtra(ConstantLib.VENUE_DATA, response)
                intentActivity.putExtra(ConstantLib.CLUB_ID, dataClub!!.clubId)
                startActivity(intentActivity)
            }else{
                Toast.makeText(this,"No venue data found",Toast.LENGTH_LONG).show()
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
        input.addProperty("club_id", dataClub!!.clubId)
        aVenueDetailsPresenterImplementation!!.callLikeUnlikeApi(input, Utility.createHeaders(sharedPreference))
    }


    private fun setupViews() {
        Glide.with(applicationContext).load(dataClub!!.clubImage).placeholder(R.drawable.blackbg).into(binding!!.htabHeader)
        binding!!.txtName.text = dataClub!!.clubName
        binding!!.txtClubType.text = dataClub!!.clubType
        binding!!.txtLocation.text = dataClub!!.address
        binding!!.txtAgegroup.text = dataClub!!.agelimit
        binding!!.imgHeart.isLiked = dataClub!!.isFavoriteVenue
        binding!!.txtDescription.text = dataClub!!.club_description
        binding!!.txtDresscode.text = dataClub!!.dress
    }

    override fun onVenueDetailsSuccess(responseData: ClubDetailResponse.Data) {
        response = responseData
    }

    override fun onLikeUnlikeSuccess(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }


}
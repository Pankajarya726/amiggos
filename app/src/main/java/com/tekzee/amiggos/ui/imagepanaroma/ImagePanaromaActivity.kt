//package com.tekzee.amiggos.ui.imagepanaroma
//
//import android.content.Intent
//import android.graphics.Bitmap
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.databinding.DataBindingUtil
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.SimpleTarget
//import com.bumptech.glide.request.transition.Transition
//import com.google.gson.JsonObject
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.databinding.ImagePanaromaBinding
//import com.tekzee.amiggos.ui.chooseweek.ChooseWeekActivity
//import com.tekzee.amiggos.ui.imagepanaroma.model.VenueDetailResponse
//import com.tekzee.amiggos.ui.venuedetails.VenueDetailsActivity
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.base.model.LanguageData
//import com.tekzee.amiggos.util.SharedPreference
//import com.tekzee.amiggos.util.Utility
//import com.tekzee.amiggos.constant.ConstantLib
//import pl.rjuszczyk.panorama.viewer.PanoramaGLSurfaceView
//
//
//class ImagePanaromaActivity: BaseActivity(), ImagePanaromaPresenter.ImagePanaromaMainView {
//
//    private var panoramaGLSurfaceView: PanoramaGLSurfaceView? = null
//    private lateinit var binding: ImagePanaromaBinding
//    private var sharedPreferences: SharedPreference? = null
//    private var languageData: LanguageData? = null
//    private var imagePanaromaPresenterImplementation: ImagePanaromaPresenterImplementation? = null
//    private var response: VenueDetailResponse? =null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.image_panaroma)
//        panoramaGLSurfaceView = findViewById(R.id.panorama) as PanoramaGLSurfaceView
//        sharedPreferences = SharedPreference(this)
//        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
//        imagePanaromaPresenterImplementation = ImagePanaromaPresenterImplementation(this,this)
//
//        setupViewData()
//        setupClickListener()
//        Glide.with(applicationContext)
//            .asBitmap()
//            .load(intent.getStringExtra(ConstantLib.PROFILE_IMAGE))
//            .placeholder(R.drawable.noimage)
//            .into(object : SimpleTarget<Bitmap>() {
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    panoramaGLSurfaceView!!.setPanoramaBitmap(resource)
//                }
//
//            })
//
//        if(intent.getIntExtra(ConstantLib.VIP_TABLE,0) == 1){
//
//            binding.imgBottle.visibility = View.VISIBLE
//        }else{
//            binding.imgBottle.visibility = View.GONE
//        }
//
//        callVenueDetailsApi()
//
//    }
//
//
//    private fun callVenueDetailsApi() {
//        val input: JsonObject = JsonObject()
//        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("club_id", intent.getStringExtra(ConstantLib.CLUB_ID))
//        imagePanaromaPresenterImplementation!!.callVenueDetailsApi(input, Utility.createHeaders(sharedPreferences))
//    }
//
//    private fun setupClickListener() {
//        binding.imgClose.setOnClickListener {
//            onBackPressed()
//        }
//
//        binding.btnVenueDetails.setOnClickListener {
//            val intent = Intent(applicationContext, VenueDetailsActivity::class.java)
//            intent.putExtra(ConstantLib.VENUE_DATA, response)
//            intent.putExtra(ConstantLib.CLUB_ID, getIntent().getStringExtra(ConstantLib.CLUB_ID))
//            startActivity(intent)
//        }
//
//        binding.imgBottle.setOnClickListener{
//
//            val intent = Intent(applicationContext, ChooseWeekActivity::class.java)
//            intent.putExtra(ConstantLib.CLUB_ID, getIntent().getStringExtra(ConstantLib.CLUB_ID))
//            startActivity(intent)
//        }
//    }
//
//    private fun setupViewData() {
//        binding.btnVenueDetails.text = languageData!!.klVenuedetails
//    }
//
//
//    override fun validateError(message: String) {
//        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
//    }
//
//
//    override fun onPause() {
//        super.onPause()
////        panoramaGLSurfaceView!!.onPause()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        panoramaGLSurfaceView!!.onResume()
//    }
//
//
//    override fun onVenueDetailsSuccess(responseData: VenueDetailResponse?) {
//        response = responseData
//        if(intent.getStringExtra(ConstantLib.IS_REGULAR_IMAGE).equals("yes",true)){
//            val intentActivity = Intent(applicationContext, VenueDetailsActivity::class.java)
//            intentActivity.putExtra(ConstantLib.VENUE_DATA, response)
//            intentActivity.putExtra(ConstantLib.CLUB_ID, intent.getStringExtra(ConstantLib.CLUB_ID))
//            startActivity(intentActivity)
//            finish()
//        }
//
//
//    }
//
//}
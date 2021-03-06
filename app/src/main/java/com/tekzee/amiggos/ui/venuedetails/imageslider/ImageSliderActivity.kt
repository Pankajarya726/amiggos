//package com.tekzee.amiggos.ui.venuedetails.imageslider
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.databinding.DataBindingUtil
//import com.smarteist.autoimageslider.IndicatorAnimations
//import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController
//import com.tekzee.amiggos.R
//import com.tekzee.amiggos.databinding.ImageSliderActivityBinding
//import com.tekzee.amiggos.ui.venuedetails.ImageClickListener
//import com.tekzee.amiggos.ui.venuedetails.ImageSliderAdapter
//import com.tekzee.amiggos.ui.videoplayer.MyPlayerActivity
//import com.tekzee.amiggos.base.BaseActivity
//import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubDetailResponse
//import com.tekzee.amiggos.constant.ConstantLib
//
//class ImageSliderActivity: BaseActivity() {
//
//    private lateinit var binding:ImageSliderActivityBinding
//    var sliderAdapter: ImageSliderAdapter? = null
//    lateinit var dataItem: ClubDetailResponse.Data.ClubData
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.image_slider_activity)
//        dataItem = intent.getSerializableExtra(ConstantLib.IMAGE_DATA) as ClubDetailResponse.Data.ClubData
//        setupSlider()
//
//        binding.imgClose.setOnClickListener{
//            onBackPressed()
//        }
//
//    }
//
//    private fun setupSlider() {
//
//        sliderAdapter = ImageSliderAdapter(dataItem.imageVideoData,object : ImageClickListener{
//            override fun onItemClick(imageVideoData: ClubDetailResponse.Data.ClubData.ImageVideoData) {
//                if(imageVideoData.isImage == 0){
//                    val intent = Intent(applicationContext, MyPlayerActivity::class.java)
//                    intent.putExtra(ConstantLib.IMAGE_DATA,imageVideoData)
//                    startActivity(intent)
//                }
//
//            }
//        })
//        binding.imageSlider.sliderAdapter = sliderAdapter
//        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.SLIDE) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//
//
//        binding.imageSlider.setOnIndicatorClickListener(DrawController.ClickListener { position ->
//            binding.imageSlider.setCurrentPagePosition(
//                position
//            )
//        })
//    }
//
//
//    override fun validateError(message: String) {
//        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
//    }
//}
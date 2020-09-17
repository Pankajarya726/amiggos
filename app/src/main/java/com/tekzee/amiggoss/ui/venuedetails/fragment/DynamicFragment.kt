package com.tekzee.amiggoss.ui.venuedetails.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.DynamicFragmentBinding
import com.tekzee.amiggoss.ui.chooseweek.ChooseWeekActivity
import com.tekzee.amiggoss.ui.venuedetails.ImageClickListener
import com.tekzee.amiggoss.ui.venuedetails.SliderAdapter
import com.tekzee.amiggoss.ui.venuedetails.fragment.adapter.DataAdapter
import com.tekzee.amiggoss.ui.venuedetails.imageslider.ImageSliderActivity
import com.tekzee.amiggoss.ui.venuedetailsnew.model.ClubDetailResponse
import com.tekzee.amiggoss.ui.videoplayer.MyPlayerActivity
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib


class DynamicFragment: BaseFragment() {


    private var clubId: String? = null
    private lateinit var adapter: DataAdapter
    lateinit var binding: DynamicFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    var sliderAdapter: SliderAdapter? = null
    var dataItem: ClubDetailResponse.Data.ClubData? =null
    companion object{
        fun newInstance(): DynamicFragment {
            return DynamicFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dynamic_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)



        val bundle = arguments
        dataItem = bundle!!.getSerializable(ConstantLib.VENUE_DATA) as ClubDetailResponse.Data.ClubData
        clubId = bundle.getString(ConstantLib.CLUB_ID)
        //Logger.d(dataItem.toString())
        sliderAdapter = SliderAdapter(dataItem!!.imageVideoData,object : ImageClickListener {
            override fun onItemClick(imageVideoData: ClubDetailResponse.Data.ClubData.ImageVideoData) {
                if(imageVideoData.isImage==1){
                    val intent = Intent(activity,ImageSliderActivity::class.java)
                    intent.putExtra(ConstantLib.IMAGE_DATA,dataItem)
                    startActivity(intent)
                }else{
                    val intent = Intent(activity,MyPlayerActivity::class.java)
                    intent.putExtra(ConstantLib.IMAGE_DATA,imageVideoData)
                    startActivity(intent)
                }


            }
        })
        binding.imageSlider.sliderAdapter = sliderAdapter
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimations.SLIDE) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!


        binding.imageSlider.setOnIndicatorClickListener(DrawController.ClickListener { position ->
            binding.imageSlider.setCurrentPagePosition(
                position
            )


        })

        setupViewData(dataItem!!)
        setupReyclerView(dataItem!!)
        setupClickListener()
        return myView
    }

    fun slideTotop(){
        binding.nestedscrollview.post(Runnable {
            binding.nestedscrollview.fullScroll(ScrollView.FOCUS_UP)
        })
    }

    private fun setupClickListener() {
        binding.imgAmiggos.setOnClickListener{
            val intent = Intent(activity,ChooseWeekActivity::class.java)
            intent.putExtra(ConstantLib.CLUB_ID,clubId)
            startActivity(intent)
        }
    }

    private fun setupReyclerView(dataItem: ClubDetailResponse.Data.ClubData) {

        binding.dataReyclerview.setHasFixedSize(true)
        binding.dataReyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = DataAdapter(dataItem.venueData)
        binding.dataReyclerview.adapter = adapter
//        binding.dataReyclerview.smoothScrollToPosition(0);
//        slideTotop()
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.M) {
            slideTotop()
        }
    }

    private fun setupViewData(dataItem: ClubDetailResponse.Data.ClubData) {
        binding.txtNameVenue.text = this.dataItem!!.clubName
    }

    override fun validateError(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }
}
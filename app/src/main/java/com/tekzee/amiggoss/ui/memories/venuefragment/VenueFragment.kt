package com.tekzee.amiggoss.ui.memories.venuefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.VenueFragmentBinding
import com.tekzee.amiggoss.ui.memories.venuefragment.adapter.VenueFragmentAdapter
import com.tekzee.amiggoss.ui.memories.venuefragment.model.VenueTaggedResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.util.Utility
import com.tekzee.amiggoss.constant.ConstantLib
import java.util.ArrayList

class VenueFragment : BaseFragment(), VenueFragmentPresenter.VenueFragmentPresenterMainView
{

    private val data: ArrayList<VenueTaggedResponse.Data.TaggedVenue> = ArrayList()
    private lateinit var binding: VenueFragmentBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var venueFragmentPresenterImplementation: VenueFragmentPresenterImplementation? = null
    private var adapter:VenueFragmentAdapter?=null

    companion object {

        fun newInstance(): VenueFragment {
            return VenueFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.venue_fragment, container, false)
       
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        venueFragmentPresenterImplementation = VenueFragmentPresenterImplementation(this,context!!)
        sharedPreference = SharedPreference(context!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupVenueTaggedRecycler()
        callTaggedVenueApi()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.error.errorLayout.setOnClickListener {
            callTaggedVenueApi()
        }
    }

    private fun setupVenueTaggedRecycler() {
        binding.aVenueRecyclerview.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity,2)
        binding.aVenueRecyclerview.layoutManager = layoutManager
        adapter = VenueFragmentAdapter(data)
        binding.aVenueRecyclerview.adapter = adapter
    }

    private fun callTaggedVenueApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        venueFragmentPresenterImplementation!!.callTaggedVenueApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    override fun onVenueResponse(taggedVenue: List<VenueTaggedResponse.Data.TaggedVenue>) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(taggedVenue)
        adapter!!.notifyDataSetChanged()
        setupErrorVisibility()
    }

    override fun onVenueFailure(message: String) {
        setupErrorVisibility()
    }


    override fun validateError(message: String) {
        setupErrorVisibility()
    }

    override fun onStop() {
        super.onStop()
        venueFragmentPresenterImplementation!!.onStop()
    }


    fun setupErrorVisibility(){
        if(data.size == 0){
            binding.error.errorLayout.visibility = View.VISIBLE
            binding.aVenueRecyclerview.visibility = View.GONE
        }else{
            binding.aVenueRecyclerview.visibility = View.VISIBLE
            binding.error.errorLayout.visibility = View.GONE
        }
    }

}
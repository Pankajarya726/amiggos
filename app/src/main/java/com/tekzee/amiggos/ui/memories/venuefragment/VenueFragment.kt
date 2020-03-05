package com.tekzee.amiggos.ui.memories.venuefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.VenueFragmentBinding
import com.tekzee.amiggos.ui.memories.venuefragment.adapter.VenueFragmentAdapter
import com.tekzee.amiggos.ui.memories.venuefragment.model.VenueTaggedResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
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
        venueFragmentPresenterImplementation = VenueFragmentPresenterImplementation(this,activity!!)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupVenueTaggedRecycler()
        callTaggedVenueApi()
        return binding.root
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
    }

    override fun onVenueFailure(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        venueFragmentPresenterImplementation!!.onStop()
    }

}
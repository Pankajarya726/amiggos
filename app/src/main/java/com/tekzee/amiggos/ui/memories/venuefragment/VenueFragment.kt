package com.tekzee.amiggos.ui.memories.venuefragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.VenueFragmentBinding
import com.tekzee.amiggos.ui.memories.ourmemories.model.MemorieResponse
import com.tekzee.amiggos.ui.memories.venuefragment.adapter.VenueFragmentAdapter
import com.tekzee.amiggos.ui.storieviewnew.StorieViewNew
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.base.BaseFragment
import java.util.*

class VenueFragment : BaseFragment(), VenueFragmentPresenter.VenueFragmentPresenterMainView {

    private val data: ArrayList<MemorieResponse.Data.Memories> = ArrayList()
    private lateinit var binding: VenueFragmentBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var venueFragmentPresenterImplementation: VenueFragmentPresenterImplementation? = null
    private var adapter: VenueFragmentAdapter? = null


    companion object {
        private val venueFragment: VenueFragment? = null


        fun newInstance(): VenueFragment {

            if (venueFragment == null) {
                return VenueFragment()
            }
            return venueFragment

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.venue_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        venueFragmentPresenterImplementation =
            VenueFragmentPresenterImplementation(this, requireContext())
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupVenueTaggedRecycler()
        callTaggedVenueApi()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.errorLayout.setOnClickListener {
            callTaggedVenueApi()
        }
    }

    private fun setupVenueTaggedRecycler() {
        binding.aVenueRecyclerview.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity, 3)
        binding.aVenueRecyclerview.layoutManager = layoutManager
        adapter = VenueFragmentAdapter(data, object : VenueItemClickListener {
            override fun onVenueItemClicked(
                itemData: MemorieResponse.Data.Memories,
                adapterPosition: Int
            ) {
                if (itemData.memory.isNotEmpty()) {

                    val showMemoryList: ArrayList<MemorieResponse.Data.Memories> = ArrayList()
                    for (position in data.indices) {
                        if (position >= adapterPosition) {
                            if (data[position].memory.size > 0) {
                                showMemoryList.add(data[position])
                            }

                        }
                    }


                    val intent = Intent(context, StorieViewNew::class.java)
                    intent.putExtra(ConstantLib.MEMORIE_DATA, itemData)
                    intent.putExtra(ConstantLib.FROM, ConstantLib.VENUEMEMORIES)
                    intent.putExtra(ConstantLib.COMPLETE_MEMORY, showMemoryList)
                    sharedPreference!!.save(ConstantLib.TYPEFROM, ConstantLib.VENUEMEMORIES)
                    intent.putExtra(ConstantLib.BACKFROM, "")
                    intent.putExtra(ConstantLib.DELETED_POSITION, 0)
                    startActivity(intent)
                }
            }
        })
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

    override fun onVenueResponse(
        taggedVenue: List<MemorieResponse.Data.Memories>,
        responseData: MemorieResponse
    ) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(taggedVenue)
        adapter!!.notifyDataSetChanged()
        setupErrorVisibility(responseData.message)
    }

    override fun onVenueFailure(message: String) {
        setupErrorVisibility(message)
    }


    override fun validateError(message: String) {
        setupErrorVisibility(message)
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    override fun onStop() {
        super.onStop()
        venueFragmentPresenterImplementation!!.onStop()
    }


    fun setupErrorVisibility(message: String) {
        if (data.size == 0) {
            binding.errorLayout.visibility = View.VISIBLE
            binding.aVenueRecyclerview.visibility = View.GONE
            binding.errortext.text = message
        } else {
            binding.aVenueRecyclerview.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
            binding.errortext.text = ""
        }
    }

}
package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.CommonFragmentLayoutBinding
import com.tekzee.amiggos.ui.guestlist.GuestListActivity
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.adapter.PartyInvitesNewAdapter
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.interfaces.PartyInviteInterface
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse

class PartyInvitesFragment: BaseFragment(), PartyInvitesPresenter.PartyInviteMainView {


    lateinit var binding: CommonFragmentLayoutBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var partyInvitesPresenterImplementation: PartyInvitesPresenterImplementation? = null
    private lateinit var adapter: PartyInvitesNewAdapter
    private val items: ArrayList<BookingInvitationResponse.Data.BookingDetail> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_fragment_layout,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(requireActivity().baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        partyInvitesPresenterImplementation = PartyInvitesPresenterImplementation(this,requireActivity())
        setupRecyclerView()
        callPartyInviteApi()
        return myView
    }

    private fun callPartyInviteApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        partyInvitesPresenterImplementation!!.doCallPartyInviteApi(input, Utility.createHeaders(sharedPreference))
    }

    private fun setupRecyclerView() {

        binding.commonRecyclerview.setHasFixedSize(true)
        binding.commonRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = PartyInvitesNewAdapter(items,languageData, object : PartyInviteInterface {
            override fun onItemClicked(
                partyinvitesData: BookingInvitationResponse.Data.BookingDetail,
                type: Int
            ) {
                when(type){

                    1 -> {
                        callJoinPartyInviteApi(partyinvitesData)
                    }
                    2 ->{
                        callDeclinePartyInvites(partyinvitesData)
                    }
                    3 ->{
                        val intent = Intent(activity,GuestListActivity::class.java)
                        intent.putExtra(ConstantLib.BOOKING_ID,partyinvitesData.bookingId.toString())
                        startActivity(intent)
                    }
                }
            }

        })
        binding.commonRecyclerview.adapter = adapter

    }

    private fun callJoinPartyInviteApi(partyinvitesData: BookingInvitationResponse.Data.BookingDetail) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", partyinvitesData.bookingId)
        partyInvitesPresenterImplementation!!.doCallJoinPartyInvites(input, Utility.createHeaders(sharedPreference))
    }


    private fun callDeclinePartyInvites(partyinvitesData: BookingInvitationResponse.Data.BookingDetail) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", partyinvitesData.bookingId)
        partyInvitesPresenterImplementation!!.doCallDeclinePartyInvites(input, Utility.createHeaders(sharedPreference))
    }


    override fun validateError(message: String) {
        items.clear()
        adapter.notifyDataSetChanged()
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    override fun onPartyInviteSuccess(responseData: BookingInvitationResponse?) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data.bookingDetails)
        adapter.notifyDataSetChanged()
    }

    override fun onJoinPartyInvitesSuccess(responseData: CommonResponse?) {
//        callPartyInviteApi()
        val intent = Intent(activity,PartyDetailsActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(activity,responseData!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onDeclinePartyInvitesSuccess(responseData: CommonResponse?) {
        //callPartyInviteApi()
        val intent = Intent(activity,PartyDetailsActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        Toast.makeText(activity,responseData!!.message,Toast.LENGTH_LONG).show()
    }



}
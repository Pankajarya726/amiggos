package com.tekzee.amiggos.ui.bookings_new.bookinginvitation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.adapter.BookingInvitationAdapter
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.interfaces.BookingInvitationInterfaces
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.BookingInvitationsFragmentBinding
import com.tekzee.amiggos.ui.bookingdetailnew.BookingDetailNewActivity
import com.tekzee.amiggos.ui.bookings_new.bookinginvitation.model.BookingInvitationResponse
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails

class BookingInvitations : BaseFragment(), BookingInvitationPresenter.BookingInvitationMainView {


    private val pageNo: Int? =0
    private val searchName: String? =""
    lateinit var binding: BookingInvitationsFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var bookingPresenterImplementation: BookingPresenterImplementation? = null
    private lateinit var adapter: BookingInvitationAdapter
    private val items: ArrayList<BookingInvitationResponse.Data.BookingDetail> = ArrayList()
    private var isFragmentVisible = false

    companion object {
        private val INVITATION: BookingInvitations? = null


        fun newInstance(): BookingInvitations {
            if(INVITATION == null){
                return BookingInvitations()
            }
            return INVITATION
        }
    }

    override fun onStart() {
        super.onStart()
        isFragmentVisible = true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.booking_invitations_fragment, container, false)
        myView = binding.root
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        bookingPresenterImplementation = BookingPresenterImplementation(this, requireContext())
        setupRecyclerView()
        callBookingApi()
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.error.errorLayout.setOnClickListener {
            items.clear()
            adapter.notifyDataSetChanged()
            callBookingApi()
        }
    }

    private fun setupRecyclerView() {

        binding.invitationRecyclerview.setHasFixedSize(true)
        binding.invitationRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = BookingInvitationAdapter(items, languageData, object :
            BookingInvitationInterfaces {
            override fun onItemClicked(
                invitationData: BookingInvitationResponse.Data.BookingDetail,
                type: Int
            ) {
                when (type) {
                    1 -> {
                        callAcceptApi(invitationData)
                    }
                    2 -> {
                        callRejectApi(invitationData)
                    }
                    3 -> {
                        val intent = Intent(activity, AProfileDetails::class.java)
                        intent.putExtra(ConstantLib.FRIEND_ID, invitationData.user_id.toString())
                        intent.putExtra(ConstantLib.FROM, ConstantLib.BOOKING_INVITATION)
                        startActivity(intent)
                    }
                    4 -> {
                        val intent = Intent(activity, BookingDetailNewActivity::class.java)
                        intent.putExtra(ConstantLib.BOOKING_ID,invitationData.bookingId.toString())
                        intent.putExtra(ConstantLib.FROM,ConstantLib.BOOKING_INVITATION)
                        context!!.startActivity(intent)
                        Animatoo.animateSlideRight(activity)
                    }
                }
            }

        })

        binding.invitationRecyclerview.adapter = adapter

    }

    private fun callRejectApi(invitationData: BookingInvitationResponse.Data.BookingDetail) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", invitationData.bookingId.toString())
        bookingPresenterImplementation!!.doRejectBookingInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callAcceptApi(invitationData: BookingInvitationResponse.Data.BookingDetail) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("booking_id", invitationData.bookingId.toString())
        bookingPresenterImplementation!!.doAcceptBookingInvitationApi(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun callBookingApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
       bookingPresenterImplementation!!.doCallBookingInvitationApi(
            input,
            Utility.createHeaders(sharedPreference),
                    isFragmentVisible
        )
    }

    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onInvitaionSuccess(responseData: BookingInvitationResponse?) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data.bookingDetails)
        adapter.notifyDataSetChanged()
        setupErrorVisibility()
    }


    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity, responseData!!.message, Toast.LENGTH_LONG).show()
        callBookingApi()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity, responseData!!.message, Toast.LENGTH_LONG).show()
        callBookingApi()
    }

    override fun onInvitationFailure(responseData: String) {
        items.clear()
        adapter.notifyDataSetChanged()
        setupErrorVisibility()
    }


    override fun onStop() {
        super.onStop()
        bookingPresenterImplementation!!.onStop()
    }



    fun setupErrorVisibility(){
        if(items.size == 0){
            binding.error.errorLayout.visibility = View.VISIBLE
            binding.invitationRecyclerview.visibility = View.GONE
        }else{
            binding.invitationRecyclerview.visibility = View.VISIBLE
            binding.error.errorLayout.visibility = View.GONE
        }
    }
}
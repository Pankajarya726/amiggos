package com.tekzee.amiggos.ui.partydetails.fragment.upcommingparty

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
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.CommonFragmentLayoutBinding
import com.tekzee.amiggos.ui.guestlist.GuestListActivity
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.adapter.PastPartyAdapter
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.interfaces.PastPartyInterface
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyData
import com.tekzee.amiggos.ui.partydetails.fragment.pastparty.model.PastPartyResponse
import com.tekzee.amiggos.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib

class UpcommingPartyFragment: BaseFragment(), UpcommingPartyPresenter.UpcomingPartyMainView {


    lateinit var binding: CommonFragmentLayoutBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var upcomingPartyPresenterImplementation: UpcomingPartyPresenterImplementation? = null
    private lateinit var adapter: PastPartyAdapter
    private val items: ArrayList<PastPartyData> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_fragment_layout,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        upcomingPartyPresenterImplementation = UpcomingPartyPresenterImplementation(this,activity!!)
        setupRecyclerView()
        callUpcomingParty()
        return myView
    }

    private fun callUpcomingParty() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        upcomingPartyPresenterImplementation!!.doCallUpcomingPartyApi(input, Utility.createHeaders(sharedPreference))
    }


    private fun setupRecyclerView() {

        binding.commonRecyclerview.setHasFixedSize(true)
        binding.commonRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = PastPartyAdapter(items,languageData, object : PastPartyInterface {
            override fun onItemClicked(
                pastPartyData: PastPartyData,
                type: Int
            ) {
                when(type){
                    1->{
                        val intent = Intent(activity, GuestListActivity::class.java)
                        intent.putExtra(ConstantLib.BOOKING_ID,pastPartyData.bookingId.toString())
                        intent.putExtra(ConstantLib.IS_PARTY_OWNER,pastPartyData.is_party_owner)
                        startActivity(intent)
                    }
                }
            }

        })
        binding.commonRecyclerview.adapter = adapter

    }



    override fun validateError(message: String) {
        items.clear()
        adapter.notifyDataSetChanged()
        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }

    override fun onUpcomingPartySuccess(responseData: PastPartyResponse) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }

}
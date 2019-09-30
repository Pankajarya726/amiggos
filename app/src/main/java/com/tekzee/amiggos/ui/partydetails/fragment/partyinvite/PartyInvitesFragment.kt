package com.tekzee.amiggos.ui.partydetails.fragment.partyinvite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.CommonFragmentLayoutBinding
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.adapter.PartyInvitesAdapter
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.interfaces.PartyInviteInterface
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesData
import com.tekzee.amiggos.ui.partydetails.fragment.partyinvite.model.PartyInvitesResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class PartyInvitesFragment: BaseFragment(), PartyInvitesPresenter.PartyInviteMainView {


    lateinit var binding: CommonFragmentLayoutBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var partyInvitesPresenterImplementation: PartyInvitesPresenterImplementation? = null
    private lateinit var adapter: PartyInvitesAdapter
    private val items: ArrayList<PartyInvitesData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.common_fragment_layout,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        partyInvitesPresenterImplementation = PartyInvitesPresenterImplementation(this,activity!!)
        setupRecyclerView()
        callPartyInviteApi()
        return myView
    }

    private fun callPartyInviteApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        partyInvitesPresenterImplementation!!.doCallPartyInviteApi(input,Utility.createHeaders(sharedPreference))
    }

    private fun setupRecyclerView() {

        binding.commonRecyclerview.setHasFixedSize(true)
        binding.commonRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = PartyInvitesAdapter(items,languageData, object : PartyInviteInterface {
            override fun onItemClicked(partyinvitesData: PartyInvitesData) {

            }

        })
        binding.commonRecyclerview.adapter = adapter

    }



    override fun validateError(message: String) {

    }

    override fun onPartyInviteSuccess(responseData: PartyInvitesResponse?) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }

}
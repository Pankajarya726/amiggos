package com.tekzee.amiggos.ui.realfriends.invitations

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
import com.tekzee.amiggos.databinding.InvitationsFragmentBinding
import com.tekzee.amiggos.ui.realfriends.invitations.adapter.InvitationAdapter
import com.tekzee.amiggos.ui.realfriends.invitations.interfaces.InvitationInterfaces
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationData
import com.tekzee.amiggos.ui.realfriends.invitations.model.InvitationResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib

class Invitations: BaseFragment(), InvitationPresenter.InvitationMainView {



    lateinit var binding: InvitationsFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var invitationPresenterImplementation: InvitationPresenterImplementation? = null
    private lateinit var adapter: InvitationAdapter
    private val items: ArrayList<InvitationData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.invitations_fragment,container,false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        invitationPresenterImplementation = InvitationPresenterImplementation(this,activity!!)
        setupRecyclerView()
        callInvitationApi()
        return myView
    }

    private fun setupRecyclerView() {

        binding.invitationRecyclerview.setHasFixedSize(true)
        binding.invitationRecyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = InvitationAdapter(items,languageData, object : InvitationInterfaces {
            override fun onItemClicked(
                invitationData: InvitationData,
                type: Int
            ) {
                when(type){
                    1->{
                        callAcceptApi(invitationData)
                    }
                    2->{
                        callRejectApi(invitationData)
                    }
                }
            }

        })
        binding.invitationRecyclerview.adapter = adapter

    }

    private fun callRejectApi(invitationData: InvitationData) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", invitationData.userid)
        invitationPresenterImplementation!!.doRejectInvitationApi(input,Utility.createHeaders(sharedPreference))
    }

    private fun callAcceptApi(invitationData: InvitationData) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("friend_id", invitationData.userid)
        invitationPresenterImplementation!!.doAcceptInvitationApi(input,Utility.createHeaders(sharedPreference))
    }

    private fun callInvitationApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        invitationPresenterImplementation!!.doCallInvitationApi(input,Utility.createHeaders(sharedPreference))
    }

    override fun validateError(message: String) {

        Toast.makeText(activity,message,Toast.LENGTH_LONG).show()
    }

    override fun onInvitaionSuccess(responseData: InvitationResponse?) {
        items.clear()
        adapter.notifyDataSetChanged()
        items.addAll(responseData!!.data)
        adapter.notifyDataSetChanged()
    }


    override fun onAcceptInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity,responseData!!.message,Toast.LENGTH_LONG).show()
        callInvitationApi()
    }

    override fun onRejectInvitation(responseData: CommonResponse?) {
        Toast.makeText(activity,responseData!!.message,Toast.LENGTH_LONG).show()
        callInvitationApi()
    }

    override fun onInvitationFailure(responseData: String) {
        items.clear()
        adapter.notifyDataSetChanged()


    }
}
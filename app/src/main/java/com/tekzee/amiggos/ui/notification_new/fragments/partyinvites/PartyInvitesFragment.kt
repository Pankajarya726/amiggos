package com.tekzee.amiggos.ui.notification_new.fragments.partyinvites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.APartyInvitesFragmentBinding
import com.tekzee.amiggos.ui.notification_new.adapter.ANotificationAdapter
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class PartyInvitesFragment: BaseFragment(), PartyInvitesFragmentPresenter.PartyInvitesFragmentPresenterMainView,
    ANotificationAdapter.HomeItemClick, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback {

    private lateinit var partyInvitesFragmentImplementation: PartyInvitesFragmentImplementation
    private var myView: View? = null
    private var binding: APartyInvitesFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: ANotificationAdapter? =null
    private var data = ArrayList<ANotificationResponse.Data.UserNotification>()
    private var pageNo = 0
    private val mLoadingData = ANotificationResponse.Data.UserNotification(loadingStatus = true)


    companion object {
        private val partyInvitesFragment: PartyInvitesFragment? = null


        fun newInstance(): PartyInvitesFragment {
            if(partyInvitesFragment == null){
                return PartyInvitesFragment()
            }
            return partyInvitesFragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.a_party_invites_fragment,container,false)
        partyInvitesFragmentImplementation=PartyInvitesFragmentImplementation(this,requireContext())
        sharedPreference = SharedPreference(requireContext())
        myView = binding!!.root
        setupReyclerview()
        setupClickListener()

        return myView
    }

    private fun setupClickListener() {
//        binding!!.errorlayout.errorLayout.setOnClickListener {
//            data.clear()
//            adapter!!.notifyDataSetChanged()
//            callNotificationApi()
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callNotificationApi(false)
    }

    private fun callNotificationApi(requestDatFromServer: Boolean) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("notification_type", "1")
        input.addProperty("user_type", sharedPreference!!.getValueString(ConstantLib.USER_TYPE))
        input.addProperty("page_no", pageNo)
        partyInvitesFragmentImplementation.doCallNotificationOne(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }




    private fun setupReyclerview() {

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.fragmentOneRecyclerview.layoutManager = layoutManager
        adapter = ANotificationAdapter(
            mContext = requireContext(),
            mRecyclerView = binding!!.fragmentOneRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = data,
            mItemClickCallback = this
        )
        binding!!.fragmentOneRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)

    }



    override fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>) {
        pageNo++
        data= responseData as ArrayList<ANotificationResponse.Data.UserNotification>
        setupReyclerview()
    }


    override fun onNotificationInfiniteSuccess(responseData:  List<ANotificationResponse.Data.UserNotification>) {
        pageNo++
        data.removeAt(data.size - 1)
        adapter?.notifyDataSetChanged()
        adapter?.setLoadingStatus(true)
        data.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onNotificationFailure(message: String) {
        Log.e("message-->",message)
    }

//    private fun setupErrorVisibility(){
//        if(data.size == 0){
//            binding!!.errorlayout.errorLayout.visibility = View.VISIBLE
//            binding!!.fragmentOneRecyclerview.visibility = View.GONE
//        }else{
//            binding!!.fragmentOneRecyclerview.visibility = View.VISIBLE
//            binding!!.errorlayout.errorLayout.visibility = View.GONE
//        }
//    }


    override fun validateError(message: String) {
        Log.e("message-->",message)
    }

    override fun itemClickCallback(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemLongClickListener(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLoadMoreData() {
        data.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callNotificationApi(true)
    }
}
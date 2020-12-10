package com.tekzee.amiggos.ui.notification_new.fragments.partyinvites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.APartyInvitesFragmentBinding
import com.tekzee.amiggos.ui.notification_new.adapter.APartyInvitesNotificationAdapter
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialog
import com.tekzee.amiggos.ui.notification_new.model.PartyInvitesNotificationResponse
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.toast
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class PartyInvitesFragment: BaseFragment(), PartyInvitesFragmentPresenter.PartyInvitesFragmentPresenterMainView,
    APartyInvitesNotificationAdapter.HomeItemClick, InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,
    KodeinAware {

    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance()
    private lateinit var partyInvitesFragmentImplementation: PartyInvitesFragmentImplementation
    private var myView: View? = null
    private var binding: APartyInvitesFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: APartyInvitesNotificationAdapter? =null
    private var data = ArrayList<PartyInvitesNotificationResponse.Data.UserNotification>()
    private var pageNo = 0
    private val mLoadingData = PartyInvitesNotificationResponse.Data.UserNotification(loadingStatus = true)


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
        adapter = APartyInvitesNotificationAdapter(
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



    override fun onNotificationSuccess(responseData: List<PartyInvitesNotificationResponse.Data.UserNotification>) {
        pageNo++
        data= responseData as ArrayList<PartyInvitesNotificationResponse.Data.UserNotification>
        Log.e("data size------>",data.size.toString())
        setupReyclerview()
    }


    override fun onNotificationInfiniteSuccess(responseData: List<PartyInvitesNotificationResponse.Data.UserNotification>) {
        pageNo++
        data.removeAt(data.size - 1)
        adapter?.notifyDataSetChanged()
        adapter?.setLoadingStatus(true)
        data.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onNotificationFailure(message: String) {
        data.removeAt(data.size - 1)
        adapter?.notifyDataSetChanged()
        adapter?.setLoadingStatus(false)
    }

    override fun onAcceptPartyRequestSuccess(message: String) {
       requireContext().toast(message)
        pageNo = 0
        data.clear()
        adapter!!.notifyDataSetChanged()
        callNotificationApi(false)
    }

    override fun onRejectPartyRequestSuccess(message: String) {
        requireActivity().toast(message)
        pageNo = 0
        data.clear()
        adapter!!.notifyDataSetChanged()
        callNotificationApi(false)
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
        requireActivity().Errortoast(message)
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageConstant.session_error)
    }

    override fun itemClickCallback(position: Int) {


        if(data[position].data.notificationKey == 5){
            val dialog: BottomDialog =
                BottomDialog.newInstance("", arrayOf(ConstantLib.ACCEPT, ConstantLib.REJECT))
            dialog.show(childFragmentManager, "dialog")
            dialog.setListener {
                if (it == 0) {
                    dialog.dismiss()
                    val input = JsonObject()
                    input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
                    input.addProperty("booking_id", data[position].data.bookingId)
                    partyInvitesFragmentImplementation.callAcceptPartyInviteApi(
                        input,
                        Utility.createHeaders(sharedPreference)
                    )
                } else
                    if (it == 1) {
                        dialog.dismiss()
                        val input = JsonObject()
                        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
                        input.addProperty("booking_id", data[position].data.bookingId)
                        partyInvitesFragmentImplementation.callRejectPartyInviteApi(
                            input,
                            Utility.createHeaders(sharedPreference)
                        )
                    }
            }
        }


    }

    override fun onItemLongClickListener(position: Int) {
         Log.e("message-->",""+position)
    }

    override fun onLoadMoreData() {
        data.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callNotificationApi(true)
    }
}
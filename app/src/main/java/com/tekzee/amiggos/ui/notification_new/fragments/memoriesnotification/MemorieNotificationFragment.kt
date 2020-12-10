package com.tekzee.amiggos.ui.notification_new.fragments.memoriesnotification

import android.content.Intent
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
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.APartyInvitesFragmentBinding
import com.tekzee.amiggos.ui.notification_new.adapter.ANotificationAdapter
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialog
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.notification_new.adapter.ANotificationAdapterFriend
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.Successtoast
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MemorieNotificationFragment : BaseFragment(),
    MemorieNotificationFragmentPresenter.MemorieNotificationFragmentPresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, ANotificationAdapter.HomeItemClick,
    KodeinAware {

    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance()
    private lateinit var memorieNotificationFragmentImplementation: MemorieNotificationFragmentImplementation
    private var myView: View? = null
    private var binding: APartyInvitesFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: ANotificationAdapter? = null
    private var data = ArrayList<ANotificationResponse.Data.UserNotification>()
    private var pageNo = 0
    private val mLoadingData = ANotificationResponse.Data.UserNotification(loadingStatus = true)

    companion object {
        private val partyInvitesFragment: MemorieNotificationFragment? = null


        fun newInstance(): MemorieNotificationFragment {
            if (partyInvitesFragment == null) {
                return MemorieNotificationFragment()
            }
            return partyInvitesFragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.a_party_invites_fragment, container, false)
        memorieNotificationFragmentImplementation =
            MemorieNotificationFragmentImplementation(this, requireContext())
        sharedPreference = SharedPreference(requireContext())
        myView = binding!!.root
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
        input.addProperty("notification_type", "3")
        input.addProperty("user_type", sharedPreference!!.getValueString(ConstantLib.USER_TYPE))
        input.addProperty("page_no", pageNo)
        memorieNotificationFragmentImplementation.doCallNotificationOne(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }

//    private fun setupReyclerview() {
//        binding!!.fragmentOneRecyclerview.setHasFixedSize(true)
//        binding!!.fragmentOneRecyclerview.layoutManager = LinearLayoutManager(context)
//        adapter = ANotificationAdapter(data,sharedPreference)
//        binding!!.fragmentOneRecyclerview.adapter = adapter
//    }

    private fun setupReyclerview() {

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
        data = responseData as ArrayList<ANotificationResponse.Data.UserNotification>
        setupReyclerview()
    }


    override fun onNotificationInfiniteSuccess(responseData: List<ANotificationResponse.Data.UserNotification>) {
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
        callNotificationApi(false)
    }

    override fun onMemoryInviteRejected(message: String) {
        pageNo = 0
        data.clear()
        adapter?.notifyDataSetChanged()
        requireActivity().Successtoast(message)
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

    override fun onLoadMoreData() {
        data.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callNotificationApi(true)
    }

    override fun itemClickCallback(position: Int) {

        if (data[position].notificationKey == 4) {
            val dialog: BottomDialog =
                BottomDialog.newInstance("", arrayOf(ConstantLib.JOIN, ConstantLib.REJECT))
            dialog.show(childFragmentManager, "dialog")
            dialog.setListener {
                if (it == 0) {
                    dialog.dismiss()
                    val intent = Intent(requireContext(), CameraActivity::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, ConstantLib.OURSTORYINVITE)
                    intent.putExtra(ConstantLib.SENDER_ID, data[position].data.sender_id.toString())
                    intent.putExtra(
                        ConstantLib.OURSTORYID,
                        data[position].data.our_story_id.toString()
                    )
                    requireContext().startActivity(intent)
                } else
                    if (it == 1) {
                        dialog.dismiss()
                        val input = JsonObject()
                        input.addProperty(
                            "userid",
                            sharedPreference!!.getValueInt(ConstantLib.USER_ID)
                        )
                        input.addProperty("sender_id", data[position].data.sender_id.toString())
                        input.addProperty(
                            "our_story_id",
                            data[position].data.our_story_id.toString()
                        )
                        memorieNotificationFragmentImplementation.doCallRejectMemoryInvite(
                            input,
                            Utility.createHeaders(sharedPreference)
                        )
                    }
            }
        }
    }

    override fun onItemLongClickListener(position: Int) {
        Log.e("message-->", "" + position)

    }
}
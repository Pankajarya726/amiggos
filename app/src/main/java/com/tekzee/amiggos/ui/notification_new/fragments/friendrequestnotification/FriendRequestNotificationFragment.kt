package com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.APartyInvitesFragmentBinding
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.notification_new.adapter.ANotificationAdapterFriend
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class FriendRequestNotificationFragment: BaseFragment(), FriendRequestFragmentPresenter.FriendRequestFragmentPresenterMainView,
    ANotificationAdapterFriend.HomeItemClick,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback {

    private lateinit var friendRequestFragmentImplementation: FriendRequestFragmentImplementation
    private var data = ArrayList<ANotificationResponse.Data.UserNotification>()
    private var myView: View? = null
    private var binding: APartyInvitesFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: ANotificationAdapterFriend? =null
    private val mLoadingData = ANotificationResponse.Data.UserNotification(loadingStatus = true)
    private var pageNo = 0


    companion object {
        private val friendRequestNotificationFragment: FriendRequestNotificationFragment? = null


        fun newInstance(): FriendRequestNotificationFragment {
            if(friendRequestNotificationFragment == null){
                return FriendRequestNotificationFragment()
            }
            return friendRequestNotificationFragment

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.a_party_invites_fragment,container,false)
        friendRequestFragmentImplementation= FriendRequestFragmentImplementation(this,requireContext())
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
        input.addProperty("notification_type", "2")
        input.addProperty("user_type", sharedPreference!!.getValueString(ConstantLib.USER_TYPE))
        input.addProperty("page_no", pageNo)
        friendRequestFragmentImplementation.doCallNotificationOne(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }

    private fun setupReyclerview() {

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding!!.fragmentOneRecyclerview.layoutManager = layoutManager
        adapter = ANotificationAdapterFriend(
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

    private fun setupErrorVisibility(){
        if(data.size == 0){
            binding!!.errorlayout.errorLayout.visibility = View.VISIBLE
            binding!!.fragmentOneRecyclerview.visibility = View.GONE
        }else{
            binding!!.fragmentOneRecyclerview.visibility = View.VISIBLE
            binding!!.errorlayout.errorLayout.visibility = View.GONE
        }
    }

    override fun validateError(message: String) {
        Log.e("message-->",message)
    }

    override fun itemClickCallback(position: Int) {
        val intent = Intent(requireContext(), AHomeScreen::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = FriendsAction.SHOW_FRIEND_REQUEST.action
        }
        startActivity(intent)
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
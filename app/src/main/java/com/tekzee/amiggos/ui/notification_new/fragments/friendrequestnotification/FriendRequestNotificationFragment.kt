package com.tekzee.amiggos.ui.notification_new.fragments.friendrequestnotification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.databinding.APartyInvitesFragmentBinding
import com.tekzee.amiggos.ui.notification_new.adapter.ANotificationAdapterFriend
import com.tekzee.amiggos.ui.notification_new.model.ANotificationResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.constant.ConstantLib

class FriendRequestNotificationFragment: BaseFragment(), FriendRequestFragmentPresenter.FriendRequestFragmentPresenterMainView {

    private lateinit var friendRequestFragmentImplementation: FriendRequestFragmentImplementation
    private var data = ArrayList<ANotificationResponse.Data.UserNotification>()
    private var myView: View? = null
    private var binding: APartyInvitesFragmentBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var adapter: ANotificationAdapterFriend? =null

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
        friendRequestFragmentImplementation= FriendRequestFragmentImplementation(this,context!!)
        sharedPreference = SharedPreference(activity!!)
        myView = binding!!.root
        setupReyclerview()
        setupClickListener()
        return myView
    }

    private fun setupClickListener() {
        binding!!.errorlayout.errorLayout.setOnClickListener {
            data.clear()
            adapter!!.notifyDataSetChanged()
            callNotificationApi()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callNotificationApi()
    }


    private fun callNotificationApi() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("notification_type", "2")
        friendRequestFragmentImplementation.doCallNotificationOne(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }

    private fun setupReyclerview() {
        binding!!.fragmentOneRecyclerview.setHasFixedSize(true)
        binding!!.fragmentOneRecyclerview.layoutManager = LinearLayoutManager(context)
        adapter = ANotificationAdapterFriend(data,sharedPreference)
        binding!!.fragmentOneRecyclerview.adapter = adapter
    }

    override fun onNotificationSuccess(responseData: List<ANotificationResponse.Data.UserNotification>) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onNotificationFailure(message: String) {
        setupErrorVisibility()
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
        setupErrorVisibility()
    }
}
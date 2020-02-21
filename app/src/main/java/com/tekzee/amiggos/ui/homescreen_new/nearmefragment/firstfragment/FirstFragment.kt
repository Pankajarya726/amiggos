package com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.firstfragment.adapter.FirstFragmentAdapter
import com.tekzee.amiggos.ui.invitefriend.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendData
import com.tekzee.amiggos.ui.searchamiggos.model.SearchFriendResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.SimpleCallback
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView


class FirstFragment : BaseFragment(), FirstFragmentPresenter.FirstFragmentPresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, FirstFragmentAdapter.HomeItemClick {

    private val mLoadingData = SearchFriendData(loadingStatus = true)
    private var sharedPreference: SharedPreference? = null
    private var lastLocation: com.google.android.gms.maps.model.LatLng? = null
    private var onlineFriendPageNo = 0
    private var firstFragmentPresenterImplementation: FirstFragmentPresenterImplementation? = null
    private var mydataList = ArrayList<SearchFriendData>()
    private var adapter: FirstFragmentAdapter? = null

    companion object {

        fun newInstance(): FirstFragment {
            return FirstFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdate()
    }

    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(activity!!, object :
            SimpleCallback<com.google.android.gms.maps.model.LatLng> {
            override fun callback(mCurrentLatLng: com.google.android.gms.maps.model.LatLng) {
                lastLocation = mCurrentLatLng
                onlineFriendPageNo = 0
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callOnlineFriendApi(false, "")
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.first_fragment, container, false)
        sharedPreference = SharedPreference(activity!!)
        firstFragmentPresenterImplementation =
            FirstFragmentPresenterImplementation(this, activity!!)
        setupViews(view)
        setupRecyclerFirstFragment(view)
        return view
    }


    private fun setupRecyclerFirstFragment(view: View) {
        val onlineFriendRecyclerview: RecyclerView =
            view.findViewById(R.id.first_fragment_recyclierview)
        val layoutManager = GridLayoutManager(activity!!, 2, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = FirstFragmentAdapter(
            mContext = activity!!,
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)
    }


    private fun callOnlineFriendApi(requestDatFromServer: Boolean, searchvalue: String) {

        InitGeoLocationUpdate.stopLocationUpdate(activity!!)
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("name", searchvalue)
        input.addProperty("global_search", "1")
        input.addProperty("page_no", onlineFriendPageNo)
        input.addProperty("latitude", lastLocation!!.latitude)
        input.addProperty("longitude", lastLocation!!.longitude)
        firstFragmentPresenterImplementation!!.getNearByUser(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }

    override fun onOnlineFriendSuccess(responseData: SearchFriendResponse?) {
        onlineFriendPageNo++
        mydataList.addAll(responseData!!.data as ArrayList<SearchFriendData>)
        adapter!!.notifyDataSetChanged()
    }

    override fun onOnlineFriendInfiniteSuccess(responseData: SearchFriendResponse?) {
        onlineFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }

    override fun onOnlineFriendFailure(responseData: String) {
        if (mydataList.size > 0) {
            adapter?.setLoadingStatus(false)
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        }
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun setupViews(view: View?) {
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOnlineFriendApi(true, "")

    }

    override fun itemClickCallback(position: Int) {
        val intent = Intent(activity, FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID, mydataList[position].userid.toString())
        intent.putExtra("from", "GroupFriendActivity")
        startActivity(intent)
    }

    override fun storieClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
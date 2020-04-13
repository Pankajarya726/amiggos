package com.tekzee.amiggos.ui.ourmemories.fragment.nearby

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.RealFriendFragmentBinding
import com.tekzee.amiggos.util.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.onlinefriends.adapter.OnlineFriendAdapter
import com.tekzee.amiggos.ui.ourmemories.OurMemoriesActivity
import com.tekzee.amiggos.ui.ourmemories.fragment.nearby.adapter.NearByAdapter
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListData
import com.tekzee.amiggos.ui.ourmemories.fragment.ourmemroiesupload.model.OurFriendListResponse
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.SimpleCallback
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class NearBy : BaseFragment(), NearByPresenter.NearByMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, OnlineFriendAdapter.HomeItemClick {


    private var lastLocation: LatLng? = null
    private val mLoadingData = OurFriendListData(loadingStatus = true)
    lateinit var binding: RealFriendFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var ourMemoriesPresenterImplementation: NearByPresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<OurFriendListData>()
    private var adapter: NearByAdapter? = null

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.real_friend_fragment, container, false)
        myView = binding.root
        sharedPreference = SharedPreference(activity!!.baseContext)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        ourMemoriesPresenterImplementation = NearByPresenterImplementation(this, activity!!)
        setupViewData()
        //callOurMemoriesApi(false, "")

        RxTextView.textChanges(binding.edtSearch).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread()).subscribe {
            realFriendPageNo = 0
            if (it.isEmpty()) {
                callOurMemoriesApi(false, "")
            } else {
                callOurMemoriesApi(false, it.toString())
            }
        }
        return myView
    }


    private fun setupViewData() {
        binding.edtSearch.hint = languageData!!.klSearchTitle
    }

    private fun callOurMemoriesApi(requestDatFromServer: Boolean, searchvalue: String) {

        if (lastLocation != null) {
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("latitude", lastLocation!!.latitude)
            input.addProperty("longitude", lastLocation!!.longitude)
            input.addProperty("global_search", "1")
            input.addProperty("our_story_id", "")
            input.addProperty("name", searchvalue)
            input.addProperty("page_no", realFriendPageNo)
            ourMemoriesPresenterImplementation!!.doCallNearBy(
                input,
                Utility.createHeaders(sharedPreference),
                requestDatFromServer
            )
        }


    }

    override fun validateError(message: String) {


    }


    override fun onOurMemoriesSuccess(responseData: OurFriendListResponse?) {
        realFriendPageNo++
        mydataList = responseData!!.data as ArrayList<OurFriendListData>

        if (mydataList.size > 0) {
            for (i in 0 until mydataList.size) {
                val userid = mydataList.get(i).userid
                if (OurMemoriesActivity.selectUserIds.contains(userid)) {
                    mydataList.get(i).is_checked = true
                }
            }

        }
        setupRecyclerRealFriend()
    }

    private fun setupRecyclerRealFriend() {
        val onlineFriendRecyclerview: RecyclerView =
            activity!!.findViewById(R.id.real_friend_fragment)
        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = NearByAdapter(
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


    override fun onOurMemoriesInfiniteSuccess(responseData: OurFriendListResponse?) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData!!.data)
        adapter?.notifyDataSetChanged()
    }


    override fun onOurMemoriesFailure(message: String) {
        adapter?.setLoadingStatus(false)
        if (mydataList.size > 0)
            mydataList.removeAt(mydataList.size - 1)
        adapter?.notifyDataSetChanged()
    }

    override fun onLoadMoreData() {
        mydataList.add(mLoadingData)
        adapter?.notifyDataSetChanged()
        callOurMemoriesApi(true, "")
    }


    override fun itemClickCallback(position: Int) {
        mydataList[position].is_checked = !mydataList[position].is_checked
        if (mydataList[position].is_checked) {

            if (!OurMemoriesActivity.selectUserIds.contains(mydataList[position].userid)) {
                OurMemoriesActivity.selectUserIds.add(mydataList[position].userid)
            }

        } else {
            if (OurMemoriesActivity.selectUserIds.contains(mydataList[position].userid)) {
                OurMemoriesActivity.selectUserIds.remove(mydataList[position].userid)
            }
        }


        adapter!!.notifyItemChanged(position)

    }

    override fun onStart() {
        super.onStart()
        startLocationUpdate()
    }

    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(activity!!, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                if (lastLocation != null) {
                    callOurMemoriesApi(false, "")
                    InitGeoLocationUpdate.stopLocationUpdate(activity!!)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        InitGeoLocationUpdate.stopLocationUpdate(activity!!)
    }

}
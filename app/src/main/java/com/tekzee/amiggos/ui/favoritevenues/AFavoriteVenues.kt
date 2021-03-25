package com.tekzee.amiggos.ui.favoritevenues

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.favoritevenues.adapter.AFavoriteVenueAdapter
import com.tekzee.amiggos.ui.favoritevenues.model.FavoriteVenueResponse
import com.tekzee.amiggos.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.RealFavoriteFragmentBinding
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.venuedetailsnew.AVenueDetails
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class AFavoriteVenues(var friendId: String?, var aProfileDetails: AProfileDetails) : BaseFragment(),
    AFavoriteVenuePresenter.AFavoriteVenuePresenterMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, AFavoriteVenueAdapter.HomeItemClick {


    private val mLoadingData = FavoriteVenueResponse.Data.FavoriteVenue(loadingStatus = true)
    lateinit var binding: RealFavoriteFragmentBinding
    private var myView: View? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var realFriendPresenterImplementation: AFavoritePresenterImplementation? = null
    private var realFriendPageNo = 0
    private var mydataList = ArrayList<FavoriteVenueResponse.Data.FavoriteVenue>()
    private var adapter: AFavoriteVenueAdapter? = null
    private lateinit var onlineFriendRecyclerview: RecyclerView


    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.real_favorite_fragment, container, false)
        myView = binding.root
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        realFriendPresenterImplementation = AFavoritePresenterImplementation(this, requireContext())
        onlineFriendRecyclerview = myView!!.findViewById(R.id.real_friend_fragment)
        setupRecyclerRealFriend()

        binding.edtSearch.setOnFocusChangeListener { view, b ->
            aProfileDetails.setupScrolling()
        }

        binding.edtSearch.setOnClickListener {
            aProfileDetails.setupScrolling()
        }

        RxTextView.textChanges(binding.edtSearch).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
            realFriendPageNo = 0
            if (it.isEmpty()) {
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callFavoriteVenue(false, "")
            } else {
                mydataList.clear()
                adapter!!.notifyDataSetChanged()
                callFavoriteVenue(false, it.toString())
            }
        }


        return myView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callFavoriteVenue(false, "")
    }

    private fun callFavoriteVenue(requestDatFromServer: Boolean, searchvalue: String) {


        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("freind_id", friendId)
        realFriendPresenterImplementation!!.doCallFavoriteVenueApi(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )

    }


    override fun validateError(message: String) {

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }


    private fun setupRecyclerRealFriend() {

        val layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false)
        onlineFriendRecyclerview.layoutManager = layoutManager
        adapter = AFavoriteVenueAdapter(
            mContext = requireContext(),
            mRecyclerView = onlineFriendRecyclerview,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = mydataList,
            mItemClickCallback = this
        )
        onlineFriendRecyclerview.adapter = adapter
        adapter?.setLoadingStatus(true)
    }


    override fun onFavoriteVenueSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>) {
        realFriendPageNo++
        mydataList.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onFavoriteVenueInfiniteSuccess(responseData: List<FavoriteVenueResponse.Data.FavoriteVenue>) {
        realFriendPageNo++
        adapter?.setLoadingStatus(true)
        mydataList.removeAt(mydataList.size - 1)
        mydataList.addAll(responseData)
        adapter?.notifyDataSetChanged()
    }

    override fun onFavoriteVenueFailure(message: String) {


        adapter?.setLoadingStatus(false)
        if (mydataList.size > 0 && realFriendPageNo > 0) {
            mydataList.removeAt(mydataList.size - 1)
            adapter?.notifyDataSetChanged()
        } else {
            mydataList.clear()
            adapter!!.notifyDataSetChanged()
        }
    }


    override fun onLoadMoreData() {
//        mydataList.add(mLoadingData)
//        adapter?.notifyDataSetChanged()
//        callFavoriteVenue(true, "")
    }


    override fun itemClickCallback(
        position: Int,
        favoriteVenue: FavoriteVenueResponse.Data.FavoriteVenue
    ) {
        val intent = Intent(activity, AVenueDetails::class.java)
        intent.putExtra(ConstantLib.VENUE_ID, favoriteVenue.clubId.toString())
        startActivity(intent)

    }


    override fun onStop() {
        super.onStop()
        realFriendPresenterImplementation!!.onStop()
    }

}
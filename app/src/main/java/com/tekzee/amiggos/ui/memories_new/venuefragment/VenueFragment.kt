package com.tekzee.amiggos.ui.memories_new.venuefragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.github.florent37.runtimepermission.PermissionResult
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.VenueFragmentBinding
import com.tekzee.amiggos.ui.home.model.NearestClub
import com.tekzee.amiggos.ui.home.model.VenueResponse
import com.tekzee.amiggos.util.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.memories_new.venuefragment.adapter.VenueFragmentAdapter
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.SimpleCallback
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class VenueFragment : BaseFragment(), VenueFragmentPresenter.VenueFragmentPresenterMainView,
InfiniteScrollRecyclerView.RecyclerViewAdapterCallback,VenueFragmentAdapter.HomeItemClick {

    private lateinit var binding: VenueFragmentBinding
    private var lastLocation: LatLng? = null
    private var currentPageVenue = 0
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var venueFragmentPresenterImplementation: VenueFragmentPresenterImplementation? = null
    private var venueListData = ArrayList<NearestClub>()
    private var venueAdapter: VenueFragmentAdapter? = null
    private val mLoadingData = NearestClub(loadingStatus = true)


    companion object {

        fun newInstance(): VenueFragment {
            return VenueFragment()
        }
    }

    override fun onStart() {
        super.onStart()
        checkForLocationPermissionFirst()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.venue_fragment, container, false)
        venueFragmentPresenterImplementation = VenueFragmentPresenterImplementation(this,activity!!)
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupRecyclerFirstFragment( binding.root)
//        checkForLocationPermissionFirst()
        return binding.root
    }


    private fun checkForLocationPermissionFirst() {
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            startLocationUpdate()
        }.onDeclined { e ->
            showExplanationDialog(e)
        }

    }

    private fun showExplanationDialog(e: PermissionResult) {
        val pDialog = SweetAlertDialog(activity)
        pDialog.titleText = getString(R.string.allow_location_permission)
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
            if (e.hasForeverDenied()) {
                e.goToSettings()
            } else {
                checkForLocationPermissionFirst()
            }
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            if (e.hasForeverDenied()) {
                e.goToSettings()
            } else {
                checkForLocationPermissionFirst()
            }
        }
        pDialog.show()
    }


    private fun startLocationUpdate() {
        InitGeoLocationUpdate.locationInit(activity!!, object :
            SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                currentPageVenue = 0
                venueListData.clear()
                venueAdapter!!.notifyDataSetChanged()
                callVenueApi(false)
            }
        })
    }

    private fun callVenueApi(isfirsttime: Boolean) {
        InitGeoLocationUpdate.stopLocationUpdate(activity!!)
        if (lastLocation != null) {
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("page_no", currentPageVenue)
            input.addProperty("latitude", lastLocation!!.latitude)
            input.addProperty("longitude", lastLocation!!.longitude)
            input.addProperty("club_type", "1")
            input.addProperty(
                "distance_filter_from",
                sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_FROM_SELECTED)
            )
            input.addProperty(
                "distance_filter_to",
                sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_TO_SELECTED)
            )
            input.addProperty(
                "filter_venue_type",
                sharedPreference!!.getValueString(ConstantLib.VENUE_TYPE_SELECTED)
            )
            input.addProperty(
                "filter_music",
                sharedPreference!!.getValueString(ConstantLib.MUSIC_SELETED)
            )
            venueFragmentPresenterImplementation!!.doGetVenueApi(
                input,
                Utility.createHeaders(sharedPreference),
                isfirsttime
            )
        } else {
            startLocationUpdate()
        }
    }

    override fun onVenueResponse(responseData: VenueResponse?) {
        currentPageVenue++
        venueListData.addAll(responseData!!.data.nearest_clubs)
        venueAdapter!!.notifyDataSetChanged()
    }

    override fun onVenueResponseInfiniteSuccess(responseData: VenueResponse?) {
        currentPageVenue++
        venueAdapter?.setLoadingStatus(true)
        venueListData.removeAt(venueListData.size - 1)
        venueListData.addAll(responseData!!.data.nearest_clubs)
        venueAdapter?.notifyDataSetChanged()

    }

    override fun onVenueFailure(message: String) {
        if (venueListData.size > 0) {
            venueAdapter?.setLoadingStatus(false)
            venueListData.removeAt(venueListData.size - 1)
            venueAdapter?.notifyDataSetChanged()
        }
    }

    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onLoadMoreData() {
        venueListData.add(mLoadingData)
        venueAdapter?.notifyDataSetChanged()
        callVenueApi(true)

    }


    private fun setupRecyclerFirstFragment(view: View) {
        val venueRecyclerView: RecyclerView =
            view.findViewById(R.id.a_venue_recyclerview)
        val layoutManager = GridLayoutManager(activity!!, 2, LinearLayoutManager.VERTICAL, false)
        venueRecyclerView.layoutManager = layoutManager
        venueAdapter = VenueFragmentAdapter(
            mContext = activity!!,
            mRecyclerView = venueRecyclerView,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = venueListData,
            mItemClickCallback = this
        )
        venueRecyclerView.adapter = venueAdapter
        venueAdapter?.setLoadingStatus(true)
    }

    override fun itemClickCallback(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun storieClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        super.onStop()
        venueFragmentPresenterImplementation!!.onStop()
    }

}
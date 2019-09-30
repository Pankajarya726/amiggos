package com.tekzee.amiggos.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.google.maps.android.ui.IconGenerator
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.HomeActivityBinding
import com.tekzee.amiggos.ui.home.adapter.HomeMyStoriesAdapter
import com.tekzee.amiggos.ui.home.adapter.HomeVenueAdapter
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.NearestClub
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.invitefriend.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.invitefriend.InviteFriendActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.mypreferences.MyPreferences
import com.tekzee.amiggos.ui.onlinefriends.OnlineFriendActivity
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.turningup.TurningUpActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.SimpleCallback
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeActivityPresenter.HomeActivityMainView,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback, HomeMyStoriesAdapter.HomeItemClick,
    OnMapReadyCallback, HomeVenueAdapter.HomeVenueItemClick {


    private var myStoriesAdapter: HomeMyStoriesAdapter? = null
    private var venueAdapter: HomeVenueAdapter? = null
    lateinit var binding: HomeActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var homeActivityPresenterImplementation: HomeActivityPresenterImplementation? = null
    private var myStoriesPageNo = 0
    private var venuePageNo = 0
    private var myStoriesListData = ArrayList<StoriesData>()
    private var venueListData = ArrayList<NearestClub>()
    private val mLoadingData = StoriesData(loadingStatus = true)
    private lateinit var mMap: GoogleMap
    private var lastLocation: LatLng? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onResume() {
        super.onResume()
        startLocationUpdate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        homeActivityPresenterImplementation = HomeActivityPresenterImplementation(this, this)
        setupView()
        setUpMap()
        setupClickListener()
        callGetMyStories(false)
    }

    private fun setupClickListener() {

        binding.imgGo.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(this, TurningUpActivity::class.java)
                startActivity(intent)
            }
        }

        binding.imgFavorite.setOnClickListener {
            checkFriedRequestSent()

        }

        binding.imgActive.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(this, OnlineFriendActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun checkFriedRequestSent(): Boolean {
        if (sharedPreference!!.getValueInt(ConstantLib.INVITE_FRIEND) > 0) {
            val intent = Intent(this, InviteFriendActivity::class.java)
            startActivity(intent)
            return true
        } else {
            return false
        }

    }


    private fun setUpMap() {

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                showExplanationDialog()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return
        } else {
            callDashboardApi()
        }

    }

    private fun showExplanationDialog() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = getString(R.string.allow_location_permission)
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        pDialog.show()
    }

    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(this, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                callDashboardApi()
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
    }


    private fun updateCamera(latitude: Double, longitude: Double) {
        val cu: CameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15.0f)
        mMap.animateCamera(cu)
    }

    private fun initMap() {

    }

    private fun callGetMyStories(requestDatFromServer: Boolean) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("is_dashboard", "1")
        input.addProperty("page_no", myStoriesPageNo)
        homeActivityPresenterImplementation!!.doGetMyStories(
            input,
            Utility.createHeaders(sharedPreference),
            requestDatFromServer
        )
    }

    private fun callDashboardApi() {
        if (lastLocation != null) {
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("latitude", lastLocation!!.latitude)
            input.addProperty("longitude", lastLocation!!.longitude)
            input.addProperty("age_group", sharedPreference!!.getValueString(ConstantLib.AGE_GROUP_SELECTED))
            input.addProperty("music", sharedPreference!!.getValueString(ConstantLib.MUSIC_SELETED))
            input.addProperty("location", myStoriesPageNo)
            input.addProperty("map_type", "1")
            input.addProperty("club_type", "1")
            input.addProperty("distance_filter_from", sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_FROM_SELECTED))
            input.addProperty("distance_filter_to", sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_TO_SELECTED))
            input.addProperty("age_filter_from", sharedPreference!!.getValueString(ConstantLib.AGE_FILTER_FROM_SELECTED))
            input.addProperty("age_filter_till", sharedPreference!!.getValueString(ConstantLib.AGE_FILTER_TILL_SELECTED))
            homeActivityPresenterImplementation!!.doGetDashboardMapApi(
                input,
                Utility.createHeaders(sharedPreference)
            )
        } else {
            startLocationUpdate()
        }
    }

    private fun callVenueApi(isfirsttime: Boolean) {
        if (lastLocation != null) {
            val input: JsonObject = JsonObject()
            input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
            input.addProperty("page_no", venuePageNo)
            input.addProperty("latitude", lastLocation!!.latitude)
            input.addProperty("longitude", lastLocation!!.longitude)
            input.addProperty("club_type", "1")
            input.addProperty("distance_filter_from", sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_FROM_SELECTED))
            input.addProperty("distance_filter_to", sharedPreference!!.getValueString(ConstantLib.DISTANCE_FILTER_TO_SELECTED))
            input.addProperty("filter_venue_type", sharedPreference!!.getValueString(ConstantLib.VENUE_TYPE_SELECTED))
            input.addProperty("filter_music", sharedPreference!!.getValueString(ConstantLib.MUSIC_SELETED))
            homeActivityPresenterImplementation!!.doGetVenueApi(
                input,
                Utility.createHeaders(sharedPreference),
                isfirsttime
            )
        } else {
            startLocationUpdate()
        }
    }

    private fun setupView() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.menu)
        drawer.addDrawerListener(toggle)
        toggle.setToolbarNavigationClickListener {
            if (!checkFriedRequestSent()) {
                drawer.openDrawer(GravityCompat.START)
            }

        }
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        setupviewData(navigationView,drawer)
        setupRecyclerMyStoriesView()
        setupRecyclerVenueView()

    }


    private fun setupRecyclerMyStoriesView() {
        val myStoriesRecyclerView: RecyclerView = findViewById(R.id.mystories_recyclerview)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        myStoriesRecyclerView.layoutManager = layoutManager
        myStoriesAdapter = HomeMyStoriesAdapter(
            mContext = this,
            mRecyclerView = myStoriesRecyclerView,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = myStoriesListData,
            mItemClickCallback = this
        )
        myStoriesRecyclerView.adapter = myStoriesAdapter
        myStoriesAdapter?.setLoadingStatus(true)
    }

    private fun setupRecyclerVenueView() {
        val venueRecyclerView: RecyclerView = findViewById(R.id.venue_recyclerview)
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        venueRecyclerView.layoutManager = layoutManager
        venueAdapter = HomeVenueAdapter(
            mContext = this,
            mRecyclerView = venueRecyclerView,
            mLayoutManager = layoutManager,
            mRecyclerViewAdapterCallback = this,
            mDataList = venueListData,
            mItemClickCallback = this
        )
        venueRecyclerView.adapter = venueAdapter
        venueAdapter?.setLoadingStatus(true)

    }


    private fun setupviewData(
        navigationView: NavigationView,
        drawer: DrawerLayout
    ) {
        val view = navigationView.getHeaderView(0)
        val name = view.findViewById(R.id.txt_name) as TextView
        name.text = sharedPreference!!.getValueString(ConstantLib.USER_NAME)
        val viewProfile = view.findViewById(R.id.txt_view_profile) as TextView
        viewProfile.text = languageData!!.klViewProfilebtn
        binding.title.text = getString(R.string.app_name)
        val userImage = view.findViewById(R.id.user_image) as ImageView
        Glide.with(this).load(sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
            .placeholder(R.drawable.user)
            .into(userImage)

        val txt_home = view.findViewById(R.id.txt_home) as TextView
        val txt_mypreference = view.findViewById(R.id.txt_mypreference) as TextView
        val txt_partydetails = view.findViewById(R.id.txt_partydetails) as TextView
        val txt_realfriends = view.findViewById(R.id.txt_realfriends) as TextView
        val txt_checkincodes = view.findViewById(R.id.txt_checkincodes) as TextView
        val txt_settings = view.findViewById(R.id.txt_settings) as TextView
        val txt_helpcenter = view.findViewById(R.id.txt_helpcenter) as TextView
        val txt_logout = view.findViewById(R.id.txt_logout) as TextView
//        val txt_venue = view.findViewById(R.id.txt_venue) as GradientTextView
//        val txt_map = view.findViewById(R.id.txt_map) as GradientTextView


        txt_home.text = languageData!!.klHome
        txt_mypreference.text = languageData!!.klMyPrefrence
        txt_partydetails.text = languageData!!.klPartyDetails
        txt_realfriends.text = languageData!!.klFriendNavTitle
        txt_checkincodes.text = languageData!!.klMyBooking
        txt_settings.text = languageData!!.klSetting
        txt_helpcenter.text = languageData!!.klHelpCenter
        txt_logout.text = languageData!!.klLogout
//        txt_map.text = languageData!!.klMaptitle
//        txt_venue.text = languageData!!.klVenues


        txt_mypreference.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,MyPreferences::class.java)
            startActivity(intent)

        }

        txt_partydetails.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,PartyDetailsActivity::class.java)
            startActivity(intent)

        }

        txt_logout.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            pDialog.titleText = "Are you sure you want to logout"
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
            }
            pDialog.setConfirmButton(languageData!!.klOk) {
                pDialog.dismiss()
                sharedPreference!!.clearSharedPreference()
                val intent = Intent(this,MainSplashActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            pDialog.show()
        }



    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onMyStoriesSuccess(responseData: GetMyStoriesResponse) {
        myStoriesPageNo++
        myStoriesListData = responseData.data
        setupRecyclerMyStoriesView()
    }


    override fun onLoadMoreData() {

        myStoriesListData.add(mLoadingData)
        myStoriesAdapter?.notifyDataSetChanged()
        callGetMyStories(true)
    }


    override fun validateError(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
    }

    override fun onMyStoriesFailure(message: String) {
        myStoriesAdapter!!.setLoadingStatus(true)
        //  Utility.showLogoutPopup(this,languageData!!,message)
    }

    override fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse) {
        myStoriesPageNo++
        myStoriesAdapter?.setLoadingStatus(true)
        myStoriesListData.removeAt(myStoriesListData.size - 1)
        myStoriesListData.addAll(responseData.data)
        myStoriesAdapter?.notifyDataSetChanged()
    }

    override fun onDashboardMapResponse(responseData: DashboardReponse?) {
        callVenueApi(false)
        setupMarkersOnMap(responseData)
        InitGeoLocationUpdate.stopLocationUpdate(this)
    }

    private fun setupMarkersOnMap(responseData: DashboardReponse?) {
        val iconGen: IconGenerator = IconGenerator(this)
        iconGen.setBackground(resources.getDrawable(R.drawable.map_profile))
        for (itemData in responseData!!.data.users) {
            val markerOptions: MarkerOptions = MarkerOptions().icon(
                BitmapDescriptorFactory.fromBitmap(createCustomMarker(this, itemData.profile))
            ).position(LatLng(itemData.latitude.toDouble(), itemData.longitude.toDouble()))
            mMap.addMarker(markerOptions)
        }

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lastLocation!!.latitude,
                    lastLocation!!.longitude
                ), 12.0f
            )
        )
    }

    fun createCustomMarker(context: Context, profile: String): Bitmap? {

        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val marker: View = layoutInflater.inflate(R.layout.custom_marker, null);
        val imageview: ImageView = marker.findViewById(R.id.marker_image) as ImageView

        Glide.with(context).load(profile).placeholder(R.drawable.user).into(imageview)


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT);
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        var bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        );
        var canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }


    override fun onVenueResponse(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?) {
        venuePageNo++
        venueListData = responseData!!.data.nearest_clubs
        setupRecyclerVenueView()
    }


    override fun onVenueResponseInfiniteSuccess(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?) {
        venuePageNo++
        venueAdapter?.setLoadingStatus(true)
        venueListData.removeAt(venueListData.size - 1)
        venueListData.addAll(responseData!!.data.nearest_clubs)
        venueAdapter?.notifyDataSetChanged()

    }


    override fun onVenueFailure(message: String) {
        venueAdapter!!.setLoadingStatus(true)
        Utility.showLogoutPopup(this, languageData!!, message)

    }

    override fun onDashboardMapFailure(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
        Utility.showLogoutPopup(this, languageData!!, message)

    }

    override fun itemVenueClickCallback(position: Int) {

        if (!checkFriedRequestSent()) {

        }
    }


    override fun itemClickCallback(position: Int) {
        if (!checkFriedRequestSent()) {

        }
    }


}

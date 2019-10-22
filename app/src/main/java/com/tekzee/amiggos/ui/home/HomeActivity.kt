package com.tekzee.amiggos.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
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
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.PermissionResult
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonObject
import com.google.maps.android.ui.IconGenerator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.HomeActivityBinding
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.helpcenter.HelpCenterActivity
import com.tekzee.amiggos.ui.home.adapter.HomeMyStoriesAdapter
import com.tekzee.amiggos.ui.home.adapter.HomeVenueAdapter
import com.tekzee.amiggos.ui.home.adapter.PaginationScrollListener
import com.tekzee.amiggos.ui.home.model.DashboardReponse
import com.tekzee.amiggos.ui.home.model.GetMyStoriesResponse
import com.tekzee.amiggos.ui.home.model.NearestClub
import com.tekzee.amiggos.ui.home.model.StoriesData
import com.tekzee.amiggos.ui.invitefriend.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.invitefriend.InviteFriendActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.mybooking.MyBookingActivity
import com.tekzee.amiggos.ui.mypreferences.MyPreferences
import com.tekzee.amiggos.ui.myprofile.MyProfileActivity
import com.tekzee.amiggos.ui.notification.NotificationActivity
import com.tekzee.amiggos.ui.onlinefriends.OnlineFriendActivity
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.realfriends.RealFriendsActivity
import com.tekzee.amiggos.ui.settings.SettingsActivity
import com.tekzee.amiggos.ui.turningup.TurningUpActivity
import com.tekzee.mallortaxi.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.SimpleCallback
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import com.tuonbondol.recyclerviewinfinitescroll.InfiniteScrollRecyclerView

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeActivityPresenter.HomeActivityMainView,

    OnMapReadyCallback, HomeVenueAdapter.HomeVenueItemClick,
    InfiniteScrollRecyclerView.RecyclerViewAdapterCallback {

    lateinit var binding: HomeActivityBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var homeActivityPresenterImplementation: HomeActivityPresenterImplementation? = null

    //adapters
    private var myStoriesAdapter: HomeMyStoriesAdapter? = null
    private var venueAdapter: HomeVenueAdapter? = null

    //data lists
    private var myStoriesListData = ArrayList<StoriesData>()
    private var venueListData = ArrayList<NearestClub>()

    private lateinit var mMap: GoogleMap
    private var lastLocation: LatLng? = null



    //loadmore item for mystories
//    private var myStoriesPageNo = 0
    private var venuePageNo = 0

    private var isLoading = false;
    private var isLastPage = false;

    private var currentPage = 0;



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val TOTAL_PAGES = 3;
    }

    override fun onStart() {
        super.onStart()
        setUpMap()
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        homeActivityPresenterImplementation = HomeActivityPresenterImplementation(this, this)
        setupView()
        setupClickListener()

        setupRecyclerMyStoriesView()
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

        binding.imgFavorite.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(this,RealFriendsActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun checkFriedRequestSent(): Boolean {
        if (sharedPreference!!.getValueInt(ConstantLib.IS_INVITE_FRIEND) == 0) {
            val intent = Intent(this, InviteFriendActivity::class.java)
            startActivity(intent)
            return true
        } else {
            return false
        }

    }



    private fun checkForLocationPermissionFirst() {
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            startLocationUpdate()
        }.onDeclined {
                e -> showExplanationDialog(e)
        }

    }


    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkForLocationPermissionFirst()
    }

    private fun showExplanationDialog(e: PermissionResult) {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        pDialog.titleText = getString(R.string.allow_location_permission)
        pDialog.setCancelable(false)
        pDialog.setCancelButton(languageData!!.klCancel) {
            pDialog.dismiss()
            if(e.hasForeverDenied()) {
                e.goToSettings()
            }else{
                checkForLocationPermissionFirst()
            }
        }
        pDialog.setConfirmButton(languageData!!.klOk) {
            pDialog.dismiss()
            if(e.hasForeverDenied()) {
                e.goToSettings()
            }else{
                checkForLocationPermissionFirst()
            }
        }
        pDialog.show()
    }

    private fun startLocationUpdate() {
        InitGeoLocationUpdate.locationInit(this, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                if(lastLocation!= null){
                    venuePageNo = 0
                    callVenueApi(false)
                    callDashboardApi()
                }else{
                    startLocationUpdate()
                }
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        mMap.setOnMarkerClickListener {
            marker -> callFriendProfileIntent(marker)
        }
    }

    private fun callFriendProfileIntent(marker: Marker): Boolean {
        val intent = Intent(applicationContext, FriendProfile::class.java)
        intent.putExtra(ConstantLib.FRIEND_ID,marker.tag.toString())
        startActivity(intent)
        return true
    }

    private fun callGetMyStories(requestDatFromServer: Boolean) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("is_dashboard", "1")
        input.addProperty("page_no", currentPage)
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
            input.addProperty("location", "")
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
        toggle.setHomeAsUpIndicator(R.drawable.menu_handburger)
        drawer.addDrawerListener(toggle)
        toggle.setToolbarNavigationClickListener {
            if (!checkFriedRequestSent()) {
                drawer.openDrawer(GravityCompat.START)
            }

        }
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        setupviewData(navigationView,drawer)
//        setupRecyclerMyStoriesView()
        //setupRecyclerVenueView()

    }


    private fun setupRecyclerMyStoriesView() {
        binding.mystoriesRecyclerview.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        binding.mystoriesRecyclerview.layoutManager = linearLayoutManager
        myStoriesAdapter = HomeMyStoriesAdapter(myStoriesListData)
        binding.mystoriesRecyclerview.adapter = myStoriesAdapter


        binding.mystoriesRecyclerview.addOnScrollListener(object: PaginationScrollListener(linearLayoutManager){
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1
                callGetMyStories(true)
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })



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
        val img_share = view.findViewById(R.id.img_share) as ImageView
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
        val txt_venue = findViewById<TextView>(R.id.txt_venue)
        val txt_map = findViewById<TextView>(R.id.txt_map)


        txt_home.text = languageData!!.klHome
        txt_mypreference.text = languageData!!.klMyPrefrence
        txt_partydetails.text = languageData!!.klPartyDetails
        txt_realfriends.text = languageData!!.klFriendNavTitle
        txt_checkincodes.text = languageData!!.klMyBooking
        txt_settings.text = languageData!!.klSetting
        txt_helpcenter.text = languageData!!.klHelpCenter
        txt_logout.text = languageData!!.klLogout
        txt_map.text = languageData!!.klMaptitle
        txt_venue.text = languageData!!.klVenues


        img_share.setOnClickListener{
            shareIntent()
        }


        binding.imgNotification.setOnClickListener{

            if (!checkFriedRequestSent()) {
                drawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this,NotificationActivity::class.java)
                startActivity(intent)
            }

        }


        viewProfile.setOnClickListener{
            val intent = Intent(this,MyProfileActivity::class.java)
            startActivity(intent)
        }


        txt_venue.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,MyPreferences::class.java)
            startActivity(intent)

        }


        txt_checkincodes.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,MyBookingActivity::class.java)
            startActivity(intent)

        }

        txt_realfriends.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,RealFriendsActivity::class.java)
            startActivity(intent)

        }
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

        txt_settings.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
        }

        txt_helpcenter.setOnClickListener{
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this,HelpCenterActivity::class.java)
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





    override fun onLoadMoreData() {

    }


    override fun validateError(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
    }

    override fun onMyStoriesFailure(message: String) {
          Utility.showLogoutPopup(this,languageData!!,message)
    }

    override fun onMyStoriesSuccess(responseData: GetMyStoriesResponse) {
        myStoriesListData.clear()
        myStoriesAdapter!!.notifyDataSetChanged()
        myStoriesListData.addAll(responseData.data)
        if (currentPage <= TOTAL_PAGES){
            myStoriesAdapter!!.addLoadingFooter()
        }else{
            isLastPage = true
        }
        myStoriesAdapter!!.notifyDataSetChanged()
    }

    override fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse) {
        myStoriesAdapter!!.removeLoadingFooter()
        isLoading = false
        myStoriesListData.addAll(responseData.data)
        myStoriesAdapter!!.notifyDataSetChanged()

        if (currentPage != TOTAL_PAGES){
            if(responseData.data.size == 0){
                isLastPage = true
            }else{
                myStoriesAdapter!!.addLoadingFooter()
            }
        }else{
            isLastPage = true
        }



    }

    override fun onDashboardMapResponse(responseData: DashboardReponse?) {
        setupMarkersOnMap(responseData)
        InitGeoLocationUpdate.stopLocationUpdate(this)
        binding.badge.setText(responseData!!.data.notification_count.toString())
    }

    private fun setupMarkersOnMap(responseData: DashboardReponse?) {
        val iconGen: IconGenerator = IconGenerator(this)
        iconGen.setBackground(resources.getDrawable(R.drawable.map_profile))
        for (itemData in responseData!!.data.users) {
            val markerOptions: MarkerOptions = MarkerOptions().icon(
                BitmapDescriptorFactory.fromBitmap(createCustomMarker(this, itemData.profile,itemData.userid.toString()))
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

    fun createCustomMarker(context: Context, profile: String,friendId: String): Bitmap? {

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
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        marker.setTag(friendId)
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
//        venueAdapter!!.setLoadingStatus(true)
        //Utility.showLogoutPopup(this, languageData!!, message)

    }

    override fun onDashboardMapFailure(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
        Utility.showLogoutPopup(this, languageData!!, message)

    }

    override fun itemVenueClickCallback(position: Int) {

        if (!checkFriedRequestSent()) {

        }
    }


    fun shareIntent(){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            sharedPreference!!.getValueString(ConstantLib.INVITE_MESSAGE)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

}

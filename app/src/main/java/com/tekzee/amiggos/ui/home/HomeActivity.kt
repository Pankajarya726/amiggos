package com.tekzee.amiggos.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.JsonObject
import com.google.maps.android.ui.IconGenerator
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.HomeActivityBinding
import com.tekzee.amiggos.enums.Actions
import com.tekzee.amiggos.services.UpdateUserLocationToServer
import com.tekzee.amiggos.ui.camera.CameraPreview
import com.tekzee.amiggos.ui.chat.model.Message
import com.tekzee.amiggos.ui.chat.myfriendchatlist.MyFriendChatActivity
import com.tekzee.amiggos.ui.friendprofile.FriendProfile
import com.tekzee.amiggos.ui.helpcenter.HelpCenterActivity
import com.tekzee.amiggos.ui.home.adapter.HomeMyStoriesAdapter
import com.tekzee.amiggos.ui.home.adapter.HomeVenueAdapter
import com.tekzee.amiggos.ui.home.adapter.NestedScrollPagination
import com.tekzee.amiggos.ui.home.adapter.PaginationScrollListener
import com.tekzee.amiggos.ui.home.model.*
import com.tekzee.amiggos.util.InitGeoLocationUpdate
import com.tekzee.amiggos.ui.invitefriend.InviteFriendActivity
import com.tekzee.amiggos.ui.mainsplash.MainSplashActivity
import com.tekzee.amiggos.ui.mybooking.MyBookingActivity
import com.tekzee.amiggos.ui.mymemories.MyMemoriesActivity
import com.tekzee.amiggos.ui.mypreferences.MyPreferences
import com.tekzee.amiggos.ui.myprofile.MyProfileActivity
import com.tekzee.amiggos.ui.notification.NotificationActivity
import com.tekzee.amiggos.ui.onlinefriends.OnlineFriendActivity
import com.tekzee.amiggos.ui.partydetails.PartyDetailsActivity
import com.tekzee.amiggos.ui.realfriends.RealFriendsActivity
import com.tekzee.amiggos.ui.settings.SettingsActivity
import com.tekzee.amiggos.ui.turningup.TurningUpActivity
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.SimpleCallback
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener,
    HomeActivityPresenter.HomeActivityMainView,
    OnMapReadyCallback {



    private var count: Int=0;
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


    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 0


    private var isLoadingVenue = false
    private var isLastpageVenue = false
    private var currentPageVenue = 0


    companion object {
        private const val TOTAL_PAGES = 5
        const val TOTAL_PAGES_VENUE = 5
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        homeActivityPresenterImplementation = HomeActivityPresenterImplementation(this, this)
        setupView()
        setupClickListener()
        setupRecyclerVenueView()
        setupRecyclerMyStoriesView()
        getAllUnreadChatCount()

        Intent(this, UpdateUserLocationToServer::class.java).also { intent ->
            intent.action = Actions.START.name
            startService(intent)
        }


    }


    private fun getAllUnreadChatCount() {
        val listOfUnreadMessageCount = ArrayList<Message>()
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("message")
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfUnreadMessageCount.clear()
                    for (messages in snapshot.children) {
                        val data = messages.value as HashMap<String, Any>
                        val messageData = Message(
                            data["isSeen"]!!.toString().toBoolean(),
                            data["msg"].toString(),
                            data["receiver"].toString(),
                            data["sender"].toString(),
                            data["timeSpam"]!!.toString().toLong()
                        )
                        if (messageData != null) {
                            if (messageData.receiver.equals(FirebaseAuth.getInstance().uid.toString()) && !messageData.isSeen) {
                                listOfUnreadMessageCount.add(
                                    Message(
                                        data["isSeen"]!!.toString().toBoolean(),
                                        data["msg"].toString(),
                                        data["receiver"].toString(),
                                        data["sender"].toString(),
                                        data["timeSpam"]!!.toString().toLong()
                                    )
                                )
                            }
                        }
                    }
                    if (listOfUnreadMessageCount.size == 0) {
                        binding.txtUnread.visibility = View.GONE
                    } else {
                        binding.txtUnread.visibility = View.VISIBLE
                    }
                    binding.txtUnread.text = listOfUnreadMessageCount.size.toString()
                }
            })
    }


    override fun onResume() {
        super.onResume()
        setUpMap()

    }


    private fun setupClickListener() {


        binding.imgChat.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(applicationContext, MyFriendChatActivity::class.java)
                startActivity(intent)
            }
        }

        binding.imgCamera.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(applicationContext, CameraPreview::class.java)
                intent.putExtra(
                    ConstantLib.PROFILE_IMAGE,
                    sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE)
                )
                intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                intent.putExtra(ConstantLib.COUNT, count)
                startActivity(intent)
            }
        }


        binding.imgStories.setOnClickListener {
            if (!checkFriedRequestSent()) {
                val intent = Intent(applicationContext, MyMemoriesActivity::class.java)
                startActivity(intent)
            }
        }


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
                val intent = Intent(this, RealFriendsActivity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun checkFriedRequestSent(): Boolean {
        if (sharedPreference!!.getValueString(ConstantLib.NO_DAY_REGISTER)!!.toInt() > 31) {
            if (sharedPreference!!.getValueInt(ConstantLib.IS_INVITE_FRIEND) == 0) {
                val intent = Intent(this, InviteFriendActivity::class.java)
                startActivity(intent)
                return true
            } else {
                return false
            }
        } else {
            return false
        }


    }


    private fun checkForLocationPermissionFirst() {
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            startLocationUpdate()
        }.onDeclined { e ->
            showExplanationDialog(e)
        }

    }


    private fun setUpMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)



        checkForLocationPermissionFirst()
    }

    private fun showExplanationDialog(e: PermissionResult) {
        val pDialog = SweetAlertDialog(this)
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
        InitGeoLocationUpdate.locationInit(this, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
                if (lastLocation != null) {

                    isLoading = false
                    isLastPage = false
                    currentPageVenue = 0
                    callVenueApi(false)

                    isLoadingVenue = false
                    isLastpageVenue = false
                    currentPage = 0
                    callGetMyStories(false)

                    callDashboardApi()

                    callgetNearByUserCount()
                } else {
                    startLocationUpdate()
                }
            }
        })
    }

    private fun callgetNearByUserCount() {
        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID).toString())
        input.addProperty("latitude", lastLocation!!.latitude)
        input.addProperty("longitude", lastLocation!!.longitude)
        homeActivityPresenterImplementation!!.getNearByUserCount(
            input,
            Utility.createHeaders(sharedPreference)
        )
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        mMap.setOnMarkerClickListener { marker ->

            callFriendProfileIntent(marker)
        }
    }

    private fun callFriendProfileIntent(marker: Marker): Boolean {
        if (!checkFriedRequestSent()) {

            Log.e(
                "Markerdata---->",
                marker.tag.toString() + "---" + marker.id.toString() + "=====" + marker.title
            )

            val intent = Intent(applicationContext, FriendProfile::class.java)
            intent.putExtra(ConstantLib.FRIEND_ID, marker.title)
            intent.putExtra("from","HomeActivity")
            startActivity(intent)
        }
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
            input.addProperty(
                "age_group",
                sharedPreference!!.getValueString(ConstantLib.AGE_GROUP_SELECTED)
            )
            input.addProperty("music", sharedPreference!!.getValueString(ConstantLib.MUSIC_SELETED))
            input.addProperty("location", "")
            input.addProperty("map_type", "1")
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
                "age_filter_from",
                sharedPreference!!.getValueString(ConstantLib.AGE_FILTER_FROM_SELECTED)
            )
            input.addProperty(
                "age_filter_till",
                sharedPreference!!.getValueString(ConstantLib.AGE_FILTER_TILL_SELECTED)
            )
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
        setupviewData(navigationView, drawer)
//        setupRecyclerMyStoriesView()
        //setupRecyclerVenueView()

    }


    private fun setupRecyclerMyStoriesView() {
        binding.mystoriesRecyclerview.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.mystoriesRecyclerview.layoutManager = linearLayoutManager
        myStoriesAdapter = HomeMyStoriesAdapter(myStoriesListData, object : StorieClickListener {
            override fun onStorieClick(storiesData: StoriesData) {
                val pDialog = SweetAlertDialog(this@HomeActivity)
                pDialog.titleText = languageData!!.klAddStoryAlert
                pDialog.setCancelable(false)
                pDialog.setCancelButton(languageData!!.klCancel) {
                    pDialog.dismiss()
                }
                pDialog.setConfirmButton(languageData!!.kllblAddStoryTitle) {
                    pDialog.dismiss()
                    val intent = Intent(applicationContext, CameraPreview::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, storiesData.imageUrl)
                    startActivity(intent)
                }
                pDialog.show()
            }
        })
        binding.mystoriesRecyclerview.adapter = myStoriesAdapter


        binding.mystoriesRecyclerview.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {
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
        venueRecyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        venueRecyclerView.layoutManager = layoutManager
        venueAdapter = HomeVenueAdapter(venueListData)
        venueRecyclerView.adapter = venueAdapter

        binding.scrollview.setOnScrollChangeListener(object :
            NestedScrollPagination(layoutManager) {

            override fun loadMoreItems() {
                isLoadingVenue = true
                currentPageVenue += 1
                callVenueApi(true)
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES_VENUE
            }

            override fun isLastPage(): Boolean {
                return isLastpageVenue
            }

            override fun isLoading(): Boolean {
                return isLoadingVenue
            }

        })


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
        val imagetrans = findViewById<ImageView>(R.id.imagetrans)
        val nestedScrollView = findViewById<NestedScrollView>(R.id.scrollview)


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



        imagetrans.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val action = event!!.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        nestedScrollView.requestDisallowInterceptTouchEvent(true)
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                    }

                    MotionEvent.ACTION_MOVE -> {
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                    else -> {
                        return true;
                    }

                }
            }
        })


        img_share.setOnClickListener {
            shareIntent()
        }


        binding.imgNotification.setOnClickListener {

            if (!checkFriedRequestSent()) {
                drawer.closeDrawer(GravityCompat.START)
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
            }

        }


        viewProfile.setOnClickListener {
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }


        txt_venue.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, MyPreferences::class.java)
            startActivity(intent)

        }


        txt_checkincodes.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, MyBookingActivity::class.java)
            startActivity(intent)

        }

        txt_realfriends.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, RealFriendsActivity::class.java)
            startActivity(intent)

        }
        txt_mypreference.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, MyPreferences::class.java)
            startActivity(intent)

        }

        txt_partydetails.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, PartyDetailsActivity::class.java)
            startActivity(intent)
        }

        txt_settings.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        txt_helpcenter.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val intent = Intent(this, HelpCenterActivity::class.java)
            startActivity(intent)
        }


        txt_logout.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
            val pDialog = SweetAlertDialog(this)
            pDialog.titleText = languageData!!.klLogoutConfirm
            pDialog.setCancelable(false)
            pDialog.setCancelButton(languageData!!.klCancel) {
                pDialog.dismiss()
            }
            pDialog.setConfirmButton(languageData!!.klOk) {
                pDialog.dismiss()
                FirebaseAuth.getInstance().signOut()
                sharedPreference!!.clearSharedPreference()
                val intent = Intent(this, MainSplashActivity::class.java)
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


    override fun validateError(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
        count = 0
    }


    private fun setupMarkersOnMap(responseData: DashboardReponse?) {
        val iconGen: IconGenerator = IconGenerator(this)
        iconGen.setBackground(resources.getDrawable(R.drawable.map_profile))
        for (itemData in responseData!!.data.users) {
            createBitmapAndAddMarker(itemData)
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


    private fun createBitmapAndAddMarker(itemData: User) {
        createCustomMarker(
            applicationContext,
            itemData.profile,
            itemData.userid.toString(),
            itemData
        )
    }


    private fun createCustomMarker(
        context: Context,
        profile: String,
        friendId: String,
        itemData: User
    ) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val marker: View = layoutInflater.inflate(R.layout.custom_marker, null);
        val imageview: ImageView = marker.findViewById(R.id.marker_image) as ImageView
//        Glide.with(this).load(profile).placeholder(R.drawable.user).into(imageview)
        Glide.with(this)
            .asBitmap()
            .load(profile)
            .placeholder(R.drawable.user)
            .into(object : CustomTarget<Bitmap>(120, 120) {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    imageview.setImageBitmap(resource)
                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    marker.layoutParams =
                        ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT);
                    marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
                    marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
                    marker.tag = friendId
                    marker.id = friendId.toInt()
                    marker.buildDrawingCache();

                    val bitmap = Bitmap.createBitmap(
                        marker.measuredWidth,
                        marker.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    marker.draw(canvas)


                    val markerOptions: MarkerOptions = MarkerOptions().icon(
                        BitmapDescriptorFactory.fromBitmap(
                            bitmap
                        )
                    ).position(LatLng(itemData.latitude.toDouble(), itemData.longitude.toDouble()))
                        .title(itemData.userid.toString())
                    mMap.addMarker(markerOptions)

                }
            })

    }


    //dashboard responses
    override fun onDashboardMapResponse(responseData: DashboardReponse?) {
        setupMarkersOnMap(responseData)
        InitGeoLocationUpdate.stopLocationUpdate(this)
        binding.badge.setText(responseData!!.data.notification_count.toString())
    }

    override fun onDashboardMapFailure(message: String) {
        InitGeoLocationUpdate.stopLocationUpdate(this)
        Utility.showLogoutPopup(this, message)
    }


    fun shareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            sharedPreference!!.getValueString(ConstantLib.INVITE_MESSAGE)
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }


    //venue responses
    override fun onVenueResponse(responseData: VenueResponse?) {

        venueListData.clear()
        venueAdapter!!.notifyDataSetChanged()
        venueListData.addAll(responseData!!.data.nearest_clubs)
        if (currentPageVenue <= TOTAL_PAGES_VENUE) {
            if (venueListData.size > 10){
                venueAdapter!!.addLoadingFooter()
                isLastpageVenue = false
            }else{
                isLastpageVenue = true
            }

        } else {
            isLastpageVenue = true
        }
        venueAdapter!!.notifyDataSetChanged()
    }

    override fun onVenueResponseInfiniteSuccess(responseData: com.tekzee.amiggos.ui.home.model.VenueResponse?) {
        venueAdapter!!.removeLoadingFooter()
        isLoadingVenue = false
        venueListData.addAll(responseData!!.data.nearest_clubs)
        venueAdapter!!.notifyDataSetChanged()

        if (currentPageVenue != TOTAL_PAGES_VENUE) {
            if (responseData.data.nearest_clubs.size == 0) {
                isLastpageVenue = true
            } else {
                venueAdapter!!.addLoadingFooter()
            }
        } else {
            isLastpageVenue = true
        }


    }

    override fun onVenueFailure(message: String) {
        Utility.showLogoutPopup(this, message)
    }


    //mystories response
    override fun onMyStoriesSuccess(responseData: GetMyStoriesResponse) {
        myStoriesListData.clear()
        myStoriesAdapter!!.notifyDataSetChanged()
        myStoriesListData.addAll(responseData.data)
        if (currentPage <= TOTAL_PAGES) {
            if (venueListData.size > 10){
                myStoriesAdapter!!.addLoadingFooter()
                isLastPage = false
            }else{
                isLastPage = true
            }

        } else {
            isLastPage = true
        }
        myStoriesAdapter!!.notifyDataSetChanged()
    }

    override fun onMyStoriesInfiniteSuccess(responseData: GetMyStoriesResponse) {
        myStoriesAdapter!!.removeLoadingFooter()
        isLoading = false
        myStoriesListData.addAll(responseData.data)
        myStoriesAdapter!!.notifyDataSetChanged()

        if (currentPage != TOTAL_PAGES) {
            if (responseData.data.size == 0) {
                isLastPage = true
            } else {
                myStoriesAdapter!!.addLoadingFooter()
            }
        } else {
            isLastPage = true
        }


    }

    override fun onMyStoriesFailure(message: String) {
        Utility.showLogoutPopup(this, message)
    }


    override fun onNearByCountSuccess(responseData: NearbyMeCountResponse?) {
        count = responseData!!.data.nearByUserCount
    }


    override fun onDestroy() {
        super.onDestroy()
        homeActivityPresenterImplementation!!.onStop()
    }


}

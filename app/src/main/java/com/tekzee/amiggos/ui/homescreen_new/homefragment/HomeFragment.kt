package com.tekzee.amiggos.ui.homescreen_new.homefragment

import `in`.madapps.placesautocomplete.PlaceAPI
import `in`.madapps.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import `in`.madapps.placesautocomplete.listener.OnPlacesDetailsListener
import `in`.madapps.placesautocomplete.model.Place
import `in`.madapps.placesautocomplete.model.PlaceDetails
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.collection.valueIterator
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.Symbol
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.cameranew.CameraActivity
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.homescreen_new.NotifyNotification
import com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter.AutoCompleteAdapter
import com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter.AutoSuggestAdapter
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.venuedetailsnew.AVenueDetails
import com.tekzee.amiggos.ui.venuedetailsnew.model.ClubData
import com.tekzee.amiggos.util.OnSwipeTouchListener
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment(), HomePresenter.HomeMainView, OnMapReadyCallback,
    PermissionsListener, Listener {
    private lateinit var notifylistner: NotifyNotification
    private var finalDataList = ArrayList<HomeApiResponse.Data.NearestClub>()
    val listOfData = ArrayList<String>()
    private var autoSuggestAdapter: AutoSuggestAdapter? = null
    private var searchText: AutoCompleteTextView? = null
    private var placesApi: PlaceAPI? = null
    private var symbolManager: SymbolManager? = null
    private var dataResponse = ArrayList<HomeApiResponse.Data.NearestClub>()
    private var longitude: String? = " 0.0"
    private var latitude: String? = "0.0"
    private lateinit var homepresenterImplementation: HomePresenterImplementation
    private var mstyle: Style? = null
    private var mmapboxMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var img_my_location: ImageView? = null
    private var sharedPreference: SharedPreference? = null
    private var categoryId: String = ""
    private var searchkeyword: String = ""
    private var languageData: LanguageData? = null
    private var easyWayLocation: EasyWayLocation? = null

    private lateinit var arrayAdapter: AutoCompleteAdapter

    companion object {
        private val homefragment: HomeFragment? = null

        fun newInstance(): HomeFragment {
            if (homefragment == null) {
                return HomeFragment()
            }
            return homefragment
        }
    }

    override fun onPause() {
        super.onPause()
        easyWayLocation!!.endUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(this.context!!, resources.getString(R.string.mapbox_token))
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homepresenterImplementation = HomePresenterImplementation(this, context!!)
        sharedPreference = SharedPreference(context!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        mapView = view.findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        easyWayLocation   = EasyWayLocation(activity, false, this)
        easyWayLocation!!.startLocation()
    }


    private fun setupGestures(view: View?) {

        view!!.findViewById<View>(R.id.viewleft).setOnTouchListener(
            object : OnSwipeTouchListener(activity) {
                override fun onSwipeLeft() {
                    Log.d("onSwipeLeft", "onSwipeLeft")
                }

                override fun onSwipeRight() {
                    val intent = Intent(activity, CameraActivity::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                    intent.putExtra(
                        ConstantLib.PROFILE_IMAGE, sharedPreference!!.getValueString(
                            ConstantLib.PROFILE_IMAGE
                        )
                    )
                    startActivity(intent)
                    Animatoo.animateSlideRight(context);  //fire the zoom animation
                }
            })


    }


    private fun setUpLanguage(view: View) {
        view.findViewById<AutoCompleteTextView>(R.id.search_txt).hint =
            languageData!!.PWhatWouldYouLikeToDo
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).hint =
            languageData!!.PCurrentLocation

    }

    private fun callApi(s: String) {
        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("latitude", latitude)
        input.addProperty("longitude", longitude)
        input.addProperty("category_id", categoryId)
        input.addProperty("search", s)
        homepresenterImplementation.searchApi(
            input,
            Utility.createHeaders(sharedPreference),
            languageData
        )

    }


    private fun setupClickListener(view: View) {
        view.findViewById<RadioButton>(R.id.img_icon_two).setOnClickListener {
            categoryId = "12"
            callHomeApi(0)

        }

        view.findViewById<RadioButton>(R.id.img_icon_one).setOnClickListener {
            categoryId = "15"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_three).setOnClickListener {
            categoryId = "14"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_four).setOnClickListener {
            categoryId = "13"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_five).setOnClickListener {
            categoryId = "16"
            callHomeApi(0)
        }
    }

    private fun setupPlaceAutoComplete(view: View) {
        placesApi =
            PlaceAPI.Builder().apiKey(resources.getString(R.string.placeapi)).build(activity!!)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setAdapter(
            PlacesAutoCompleteAdapter(activity!!, placesApi!!)
        )
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setOnClickListener {
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText)
                .setSelectAllOnFocus(true)
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).selectAll()
        }

        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                hideKeyboard()
                val place = parent.getItemAtPosition(position) as Place
                view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText)
                    .setText(place.description)
                callPlaceApiDetails(place.id)
            }


    }

    private fun callPlaceApiDetails(id: String) {
        placesApi!!.fetchPlaceDetails(id, object : OnPlacesDetailsListener {
            override fun onError(errorMessage: String) {
                Log.d("error--->", errorMessage)
            }

            override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                latitude = placeDetails.lat.toString()
                longitude = placeDetails.lng.toString()
                activity!!.runOnUiThread(Runnable {
                    hideKeyboard()
                    val position = CameraPosition.Builder()
                        .target(LatLng(latitude!!.toDouble(), longitude!!.toDouble())).zoom(12.0)
                        .bearing(0.0).tilt(30.0).build()
                    mmapboxMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(position),
                        3000
                    )
                    callHomeApi(0)
                })

            }

        })
    }


    private fun callHomeApi(callFrom: Int) {
        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("latitude", latitude)
        input.addProperty("longitude", longitude)
        input.addProperty("category_id", categoryId)
        input.addProperty("search", searchkeyword)
        homepresenterImplementation.doCallHomeApi(
            input,
            Utility.createHeaders(sharedPreference),
            languageData,
            callFrom
        )
    }

    private fun setupViews(view: View?) {


        img_my_location = view!!.findViewById(R.id.img_my_location)
        img_my_location!!.setOnClickListener {
            enableLocationComponent(mstyle!!)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView!!.onStop()
        homepresenterImplementation.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onHomeApiSuccess(responseData: HomeApiResponse?) {

        if (dataResponse.isNotEmpty()) {
            removeAllMarkers()
            dataResponse.clear()
        }
        ConstantLib.NOTIFICATIONCOUNT = responseData!!.data.notificationcount
        dataResponse.addAll(responseData!!.data.nearestClubs)
        for (userData in responseData.data.nearestUser) {
            dataResponse.add(
                HomeApiResponse.Data.NearestClub(
                    userData.userid,
                    2,
                    userData.name,
                    "",
                    userData.distanceFromMylocation,
                    userData.profile,
                    "",
                    userData.latitude,
                    userData.longitude,
                    "",
                    "",
                    "",
                    "",
                    false
                    ,
                    ""
                )
            )
        }


        Collections.sort(dataResponse, MarkerCompartor())

        if (dataResponse.isNotEmpty()) {
            setMarkersOnMap(dataResponse)
            val latLngBounds = LatLngBounds.Builder()
                .include(
                    LatLng(
                        dataResponse[0].latitude.toDouble(),
                        dataResponse[0].longitude.toDouble()
                    )
                )
                .include(
                    LatLng(
                        dataResponse[dataResponse.size - 1].latitude.toDouble(),
                        dataResponse[dataResponse.size - 1].longitude.toDouble()
                    )
                )
                .build()
            mmapboxMap!!.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200), 3000)


        }
        notifylistner.onNotify()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notifylistner = activity as NotifyNotification
    }


    private fun removeAllMarkers() {
        val listOfSymbols = ArrayList<Symbol>()
        val symbolArray: androidx.collection.LongSparseArray<Symbol>? = symbolManager!!.annotations
        for (item in symbolArray!!.valueIterator()) {
            listOfSymbols.add(item)
        }
        symbolManager!!.delete(listOfSymbols)
    }

    private fun setMarkersOnMap(nearestClubs: List<HomeApiResponse.Data.NearestClub>) {
        if (symbolManager != null) {
            removeAllMarkers()
        }

        symbolManager = SymbolManager(mapView!!, mmapboxMap!!, mstyle!!)
        symbolManager!!.iconAllowOverlap = true
        symbolManager!!.iconIgnorePlacement = true

        for (items in nearestClubs) {
            addSymbolOnMap(
                symbolManager!!,
                items.latitude.toDouble(),
                items.longitude.toDouble(),
                items.image,
                mstyle!!,
                items.clubName,
                items
            )
        }

        symbolManager!!.addClickListener { t: Symbol? ->
            val dataObject = t!!.data!!.asJsonObject
            if (dataObject.get("type").toString() == "2") {
                val intent = Intent(activity, AProfileDetails::class.java)
                intent.putExtra(ConstantLib.FRIEND_ID, dataObject.get("clubId").toString())
                intent.putExtra(ConstantLib.PROFILE_IMAGE, dataObject.get("clubImage").toString())
                startActivity(intent)
                Animatoo.animateSlideRight(activity)
            } else {
                val intent = Intent(activity, AVenueDetails::class.java)
                val clubdata = ClubData()
                clubdata.clubName = dataObject.get("clubName").asString
                clubdata.clubBasicDetails = dataObject.get("clubId").asString
                clubdata.clubStateCity = dataObject.get("clubId").asString
                clubdata.clubType = dataObject.get("clubType").asString
                clubdata.clubId = dataObject.get("clubId").asString
                clubdata.clubImage = dataObject.get("clubImage").asString
                clubdata.address = dataObject.get("address").asString
                clubdata.agelimit = dataObject.get("agelimit").asString
                clubdata.isFavoriteVenue = dataObject.get("isFavoriteVenue").asBoolean
                clubdata.dress = dataObject.get("dress").asString
                clubdata.club_description = dataObject.get("club_description").asString

                intent.putExtra(ConstantLib.VENUE_DATA, clubdata)
                startActivity(intent)
            }

        }

    }


    private fun addSymbolOnMap(
        symbolManager: SymbolManager,
        latitude: Double,
        longitude: Double,
        image: String,
        mstyle: Style,
        clubName: String,
        items: HomeApiResponse.Data.NearestClub
    ) {
        var marker: View? = null
        var imageview: ImageView? = null
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        if (items.type == 2) {
            marker = layoutInflater.inflate(R.layout.custom_marker_circular, null)
            imageview = marker.findViewById(R.id.marker_image) as ImageView
        } else {
            marker = layoutInflater.inflate(R.layout.custom_marker_circular_user, null)
            imageview = marker.findViewById(R.id.marker_image) as ImageView
        }
        Glide.with(this)
            .asBitmap()
            .load(image)
            .placeholder(R.drawable.user)
            .into(object : CustomTarget<Bitmap>(120, 120) {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    imageview!!.setImageBitmap(resource)
                    val displayMetrics = DisplayMetrics()
                    activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
                    marker!!.layoutParams =
                        ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
                    marker!!.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
                    marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
                    marker.buildDrawingCache()
                    val bitmap = Bitmap.createBitmap(
                        marker.measuredWidth,
                        marker.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    marker.draw(canvas)
                    mstyle.addImage(clubName, bitmap)
                    val jsonData = JsonObject()
                    jsonData.addProperty("clubId", items.clubId)
                    jsonData.addProperty("type", items.type)
                    jsonData.addProperty("clubName", items.clubName)
                    jsonData.addProperty("clubType", items.clubType)
                    jsonData.addProperty("isAmigoClub", items.isAmigoClub)
                    jsonData.addProperty("latitude", items.latitude)
                    jsonData.addProperty("longitude", items.longitude)
                    jsonData.addProperty("clubType", items.clubType)
                    jsonData.addProperty("clubImage", items.image)
                    jsonData.addProperty("address", items.address)
                    jsonData.addProperty("isFavoriteVenue", items.isFavoriteVenue)
                    jsonData.addProperty("agelimit", items.agelimit)
                    jsonData.addProperty("dress", items.dress)
                    jsonData.addProperty("club_description", items.club_description)
                    symbolManager.create(
                        SymbolOptions()
                            .withLatLng(LatLng(latitude, longitude))
                            .withIconImage(clubName)
                            .withData(jsonData)
                            .withIconSize(1.0f)

                    )

                }
            })

    }


    override fun onHomeApiFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mmapboxMap = mapboxMap
        mapboxMap.setStyle(Style.DARK, fun(style: Style) {
            //enableLocationComponent(style)
            mstyle = style
        })


        setupViews(view)
        setupPlaceAutoComplete(view!!)
        setupClickListener(view!!)
        setUpLanguage(view!!)
        setupGestures(view)
        setupSearchAutocomplete()


    }

    @SuppressLint("CheckResult")
    private fun setupSearchAutocomplete() {
        searchText = view!!.findViewById<AutoCompleteTextView>(R.id.search_txt)

        RxTextView.textChanges(searchText!!).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                callApi("")
                if (it.isEmpty()) {
                    view!!.findViewById<AutoCompleteTextView>(R.id.search_txt).hint =
                        languageData!!.PWhatWouldYouLikeToDo
                } else {
                    if(it.toString().length!=0)
                    callApi("")
//                it.toString()
                }
            }

        autoSuggestAdapter = AutoSuggestAdapter(
            activity!!,
            android.R.layout.simple_dropdown_item_1line
        )
        searchText!!.setThreshold(2)
        searchText!!.setAdapter(autoSuggestAdapter)
        searchText!!.setOnItemClickListener { parent, view, position, id ->
            hideKeyboard()
            searchText!!.setText("")
            Log.d("result--->", ""+finalDataList.get(position))
            removeAllMarkers()
            setMarkersOnMap(listOf(finalDataList[position]))
            val position = CameraPosition.Builder()
                .target(LatLng(finalDataList[position].latitude.toDouble(), finalDataList[position].longitude.toDouble())).zoom(12.0)
                .bearing(0.0).tilt(30.0).build()
            mmapboxMap!!.animateCamera(
                CameraUpdateFactory.newCameraPosition(position),
                3000
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private fun enableLocationComponent(style: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) { // Get an instance of the component

            // Get an instance of the component
            val locationComponent = mmapboxMap!!.locationComponent

            // Activate with a built LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    activity!!,
                    style
                ).build()
            )

            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS

            locationComponent.isLocationComponentEnabled = true
            locationComponent.zoomWhileTracking(10.0)


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(activity)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(activity, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    override fun onPermissionResult(granted: Boolean) {

        if (granted) {
            mmapboxMap!!.getStyle { style -> enableLocationComponent(style) }
        } else {
            Toast.makeText(activity, "user_location_permission_not_granted", Toast.LENGTH_LONG)
                .show()
            activity!!.finish()
        }
    }

    override fun locationCancelled() {
        Logger.d("Location cancelled called")
    }

    override fun locationOn() {
        Logger.d("Location on called")
    }

    override fun currentLocation(location: Location?) {
        latitude = location!!.latitude.toString()
        longitude = location.longitude.toString()
        easyWayLocation!!.endUpdates()
        Logger.d("Location updates--->" + latitude + "----" + longitude)
        callHomeApi(0)
    }

    override fun onSearchApiSuccess(responseData: HomeApiResponse?) {
        finalDataList.clear()
        finalDataList.addAll(responseData!!.data.nearestClubs)
        listOfData.clear()
        autoSuggestAdapter!!.notifyDataSetChanged()
        for (x in finalDataList.indices) {
            listOfData.add(x.toString() + "-" + responseData.data.nearestClubs[x].clubName)
        }

        autoSuggestAdapter!!.setData(listOfData);
        autoSuggestAdapter!!.notifyDataSetChanged()

    }



}
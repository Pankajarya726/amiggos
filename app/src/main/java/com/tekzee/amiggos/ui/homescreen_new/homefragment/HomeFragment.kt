package com.tekzee.amiggos.ui.homescreen_new.homefragment

import `in`.madapps.placesautocomplete.PlaceAPI
import `in`.madapps.placesautocomplete.adapter.PlacesAutoCompleteAdapter
import `in`.madapps.placesautocomplete.listener.OnPlacesDetailsListener
import `in`.madapps.placesautocomplete.model.Place
import `in`.madapps.placesautocomplete.model.PlaceDetails
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.google.gson.JsonObject
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
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.ui.camera.CameraPreview
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeApiResponse
import com.tekzee.amiggos.util.*
import com.tekzee.mallortaxi.base.BaseFragment
import com.tekzee.mallortaxiclient.constant.ConstantLib


class HomeFragment : BaseFragment(), HomePresenter.HomeMainView, OnMapReadyCallback,
    PermissionsListener {

    private lateinit var placesApi: PlaceAPI
    private var symbolManager: SymbolManager? =null
    private var dataResponse = ArrayList<HomeApiResponse.Data.NearestClub>()
    private var longitude: String? = " 0.0"
    private var latitude: String? = "0.0"
    private lateinit var homepresenterImplementation: HomePresenterImplementation
    private lateinit var mstyle: Style
    private lateinit var mmapboxMap: MapboxMap
    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var img_my_location: ImageView? = null
    private var sharedPreference: SharedPreference? = null
    private var categoryId: String = ""
    private var searchkeyword: String = ""
    private var languageData: LanguageData? = null


    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
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
        sharedPreference = SharedPreference(activity!!)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        homepresenterImplementation = HomePresenterImplementation(this, activity!!)
        mapView = view.findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        setupViews(view)
        startLocationUpdate()
        setupPlaceAutoComplete(view)
        setupClickListener(view)
        setUpLanguage(view)
        setupGestures(view)
        return view
    }

    private fun setupGestures(view: View?) {

        view!!.findViewById<View>(R.id.viewleft).setOnTouchListener(
            object : OnSwipeTouchListener(activity) {
                override fun onSwipeLeft() {
                    Log.d("onSwipeLeft","onSwipeLeft")
                }

                override fun onSwipeRight() {
                    val intent = Intent(activity, CameraPreview::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE))
                    startActivity(intent)
                    Animatoo.animateSlideRight(context);  //fire the zoom animation
                }
            })


    }


    private fun setUpLanguage(view: View) {
        view.findViewById<TextView>(R.id.search_txt).text = languageData!!.PWhatWouldYouLikeToDo
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).hint = languageData!!.PCurrentLocation
    }


    private fun setupClickListener(view: View) {
        view.findViewById<ImageView>(R.id.img_icon_two).setOnClickListener {
            categoryId = "12"
            callHomeApi(0)
        }

        view.findViewById<ImageView>(R.id.img_icon_one).setOnClickListener {
            categoryId = "15"
            callHomeApi(0)
        }

        view.findViewById<ImageView>(R.id.img_icon_three).setOnClickListener {
            categoryId = "14"
            callHomeApi(0)
        }

        view.findViewById<ImageView>(R.id.img_icon_four).setOnClickListener {
            categoryId = "13"
            callHomeApi(0)
        }

        view.findViewById<ImageView>(R.id.img_icon_five).setOnClickListener {
            categoryId = "16"
            callHomeApi(0)
        }
    }

    private fun setupPlaceAutoComplete(view: View) {
        placesApi =
            PlaceAPI.Builder().apiKey("AIzaSyCBAPM9f4Xzl_Bmv-pqaYi_UAbn5JISYU4").build(activity!!)
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setAdapter(
            PlacesAutoCompleteAdapter(activity!!, placesApi)
        )
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setOnClickListener{
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setSelectAllOnFocus(true)
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
        placesApi.fetchPlaceDetails(id, object : OnPlacesDetailsListener {
            override fun onError(errorMessage: String) {
                Log.d("error--->", errorMessage)
            }

            override fun onPlaceDetailsFetched(placeDetails: PlaceDetails) {
                latitude = placeDetails.lat.toString()
                longitude = placeDetails.lng.toString()
                activity!!.runOnUiThread(Runnable {
                    hideKeyboard()
                    val position = CameraPosition.Builder().target(LatLng(latitude!!.toDouble(),longitude!!.toDouble())).zoom(12.0).bearing(0.0).tilt(30.0).build()
                    mmapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),3000)


                    callHomeApi(0)
                })

            }

        })
    }

    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(activity!!, object :
            SimpleCallback<com.google.android.gms.maps.model.LatLng> {
            override fun callback(mCurrentLatLng: com.google.android.gms.maps.model.LatLng) {
                InitGeoLocationUpdate.stopLocationUpdate(activity!!)
                latitude = mCurrentLatLng.latitude.toString()
                longitude = mCurrentLatLng.longitude.toString()
                callHomeApi(0)
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
            enableLocationComponent(mstyle)
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

        if(dataResponse.isNotEmpty()){
            removeAllMarkers()
            dataResponse.clear()
        }
        dataResponse.addAll(responseData!!.data.nearestClubs)
        if(dataResponse.isNotEmpty()){
            setMarkersOnMap(dataResponse)
            val latLngBounds = LatLngBounds.Builder()
                .include(LatLng(dataResponse[0].latitude.toDouble(),dataResponse[0].longitude.toDouble()))
                .include(LatLng(dataResponse[dataResponse.size-1].latitude.toDouble(),dataResponse[dataResponse.size-1].longitude.toDouble()))
                .build()
            mmapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,200),3000)


        }

    }

    private fun removeAllMarkers() {
        val listOfSymbols = ArrayList<Symbol>()
        val symbolArray: androidx.collection.LongSparseArray<Symbol>? = symbolManager!!.annotations
        for (item in symbolArray!!.valueIterator()){
            listOfSymbols.add(item)
        }
        symbolManager!!.delete(listOfSymbols)
    }

    private fun setMarkersOnMap(nearestClubs: List<HomeApiResponse.Data.NearestClub>) {
        if(symbolManager!=null){
            removeAllMarkers()
        }

        symbolManager = SymbolManager(mapView!!, mmapboxMap, mstyle)
        symbolManager!!.iconAllowOverlap = true
        symbolManager!!.iconIgnorePlacement = true

        for (items in nearestClubs) {
            addSymbolOnMap(
                symbolManager!!,
                items.latitude.toDouble(),
                items.longitude.toDouble(),
                items.image,
                mstyle,
                items.clubName,
                items
            )
        }

        symbolManager!!.addClickListener { t: Symbol? ->

            Log.d("id --->", "" + t!!.data.toString())
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



        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val marker: View = layoutInflater.inflate(R.layout.custom_marker, null)
        val imageview: ImageView = marker.findViewById(R.id.marker_image) as ImageView
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
                    imageview.setImageBitmap(resource)
                    val displayMetrics = DisplayMetrics()
                    activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
                    marker.layoutParams =
                        ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
                    marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
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
                    jsonData.addProperty("clubId",items.clubId)
                    jsonData.addProperty("clubName",items.clubName)
                    jsonData.addProperty("clubType",items.clubType)
                    jsonData.addProperty("isAmigoClub",items.isAmigoClub)
                    jsonData.addProperty("latitude",items.latitude)
                    jsonData.addProperty("longitude",items.longitude)
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
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


    override fun validateError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mmapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS, fun(style: Style) {
            enableLocationComponent(style)
            mstyle = style
        })
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
            val locationComponent = mmapboxMap.locationComponent

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
            mmapboxMap.getStyle { style -> enableLocationComponent(style) }
        } else {
            Toast.makeText(activity, "user_location_permission_not_granted", Toast.LENGTH_LONG)
                .show()
            activity!!.finish()
        }
    }


}
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
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.allenliu.badgeview.BadgeFactory
import com.allenliu.badgeview.BadgeView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.Listener
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.homescreen_new.CustomInfoWindowAdapter
import com.tekzee.amiggos.ui.homescreen_new.NotifyNotification
import com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter.AutoCompleteAdapter
import com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter.AutoSuggestAdapter
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse.Data.Venue
import com.tekzee.amiggos.ui.profiledetails.AProfileDetails
import com.tekzee.amiggos.ui.venuedetailsnew.AVenueDetails
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggos.util.OnSwipeTouchListener
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxi.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class HomeFragment : BaseFragment(), HomePresenter.HomeMainView,
    PermissionsListener, Listener, com.google.android.gms.maps.OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var notifylistner: NotifyNotification
    private var finalDataList = ArrayList<Venue>()
    val listOfData = ArrayList<String>()
    private var autoSuggestAdapter: AutoSuggestAdapter? = null
    private var searchText: AutoCompleteTextView? = null
    private var placesApi: PlaceAPI? = null
    private var symbolManager: SymbolManager? = null
    private var dataResponse = ArrayList<Venue>()
    private var longitude: String? = " 0.0"
    private var latitude: String? = "0.0"
    private lateinit var homepresenterImplementation: HomePresenterImplementation

    private var permissionsManager: PermissionsManager? = null
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
//        mapView!!.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homepresenterImplementation = HomePresenterImplementation(this, requireContext())
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        easyWayLocation = EasyWayLocation(activity, false, this)
        easyWayLocation!!.startLocation()
    }


    private fun setupGestures(view: View?) {

        requireView().findViewById<View>(R.id.viewleft).setOnTouchListener(
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
            languageData!!.pWhatWouldYouLikeToDo
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).hint =
            languageData!!.pCurrentLocation

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
            categoryId = "5"
            callHomeApi(0)
            setupRadioButton(R.id.img_icon_two)
        }

        view.findViewById<RadioButton>(R.id.img_icon_one).setOnClickListener {
            categoryId = "4"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_three).setOnClickListener {
            categoryId = "3"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_four).setOnClickListener {
            categoryId = "1"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_five).setOnClickListener {
            categoryId = "2"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_six).setOnClickListener {
            categoryId = "37"
            callHomeApi(0)
        }
    }

    fun setupRadioButton(id: Int) {
        requireView().findViewById<RadioButton>(id).isChecked = true
        requireView().findViewById<RadioButton>(id).isChecked = true
        requireView().findViewById<RadioButton>(id).isChecked = true
        requireView().findViewById<RadioButton>(id).isChecked = true
    }

    private fun setupPlaceAutoComplete(view: View) {
        placesApi =
            PlaceAPI.Builder().apiKey(resources.getString(R.string.placeapi))
                .build(requireContext())
        view.findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setAdapter(
            PlacesAutoCompleteAdapter(requireContext(), placesApi!!)
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
                requireActivity().runOnUiThread(Runnable {
                    hideKeyboard()
//                    val position = CameraPosition.Builder()
//                        .target(LatLng(latitude!!.toDouble(), longitude!!.toDouble())).zoom(12.0)
//                        .bearing(0.0).tilt(30.0).build()
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


    override fun onStop() {
        super.onStop()
        homepresenterImplementation.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onHomeApiSuccess(responseData: HomeResponse) {

        if (dataResponse.isNotEmpty()) {
            mMap!!.clear()
            dataResponse.clear()
        }
//        ConstantLib.NOTIFICATIONCOUNT = responseData!!.data.notificationcount
        dataResponse.addAll(responseData.data.venue)
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
            val padding = 200 // offset from edges of the map in pixels
            val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding)
            mMap!!.moveCamera(cu)


        }
        notifylistner.onNotify()
    }

    private fun setMarkersOnMap(nearestClubs: java.util.ArrayList<Venue>) {
        for (items in nearestClubs) {
            var marker: View? = null
            var imageview: ImageView? = null
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            if (items.type.equals("venue")) {
                marker = layoutInflater.inflate(R.layout.custom_marker_circular, null)
                imageview = marker.findViewById(R.id.marker_image) as ImageView
                BadgeFactory.create(requireContext())
                    .setTextColor(resources.getColor(R.color.white))
                    .setWidthAndHeight(20, 20)
                    .setBadgeBackground(resources.getColor(R.color.red))
                    .setTextSize(8)
                    .setBadgeGravity(Gravity.RIGHT)
                    .setBadgeCount(items.nearByCount)
                    .setShape(BadgeView.SHAPE_CIRCLE)
                    .setSpace(10, 10)
                    .bind(imageview)
            } else {
                marker = layoutInflater.inflate(R.layout.custom_marker_circular_user, null)
                imageview = marker.findViewById(R.id.marker_image) as ImageView


            }



            Glide.with(this)
                .asBitmap()
                .load(items.image)
                .placeholder(R.drawable.noimage)
                .into(object : CustomTarget<Bitmap>(120, 120) {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        mMap!!.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    items.latitude.toDouble(),
                                    items.longitude.toDouble()
                                )
                            )
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        getMarkerBitmapFromView(
                                            marker,
                                            resource,
                                            imageview
                                        )
                                    )
                                )
                        ).tag = items
                    }
                })

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        notifylistner = activity as NotifyNotification
    }

    private fun getMarkerBitmapFromView(view: View, bitmap: Bitmap, imageview: ImageView): Bitmap? {

        imageview.setImageBitmap(bitmap)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            view.measuredWidth, view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = view.background
        drawable?.draw(canvas)
        view.draw(canvas)
        return returnedBitmap
    }


    override fun onHomeApiFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }    override fun validateError(message: String) {



        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


    @SuppressLint("CheckResult")
    private fun setupSearchAutocomplete() {
        searchText = requireView().findViewById<AutoCompleteTextView>(R.id.search_txt)

        RxTextView.textChanges(searchText!!).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                callApi("")
                if (it.isEmpty()) {
                    requireView().findViewById<AutoCompleteTextView>(R.id.search_txt).hint =
                        languageData!!.pWhatWouldYouLikeToDo
                } else {
                    if (it.toString().length != 0)
                        callApi("")
//                it.toString()
                }
            }

        autoSuggestAdapter = AutoSuggestAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
        searchText!!.threshold = 2
        searchText!!.setAdapter(autoSuggestAdapter)
        searchText!!.setOnItemClickListener { parent, view, position, id ->
            hideKeyboard()
            searchText!!.setText("")
            Log.d("result--->", "" + finalDataList.get(position))
            mMap!!.clear()
            val datalist = ArrayList<Venue>()
            datalist.add(finalDataList[position])
            setMarkersOnMap(datalist)
            val latLngBounds = LatLngBounds.Builder()
                .include(
                    LatLng(
                        finalDataList[position].latitude.toDouble(),
                        finalDataList[position].longitude.toDouble()
                    )
                )
                .build()
            val padding = 50 // offset from edges of the map in pixels
            val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding)
            mMap!!.moveCamera(cu)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        permissionsManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(activity, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    override fun onPermissionResult(granted: Boolean) {

//        if (granted) {
//            mmapboxMap!!.getStyle { style -> enableLocationComponent(style) }
//        } else {
//            Toast.makeText(activity, "user_location_permission_not_granted", Toast.LENGTH_LONG)
//                .show()
//            requireActivity().finish()
//        }
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
        Logger.d("Location updates--->$latitude----$longitude")
        callHomeApi(0)
    }

    override fun onSearchApiSuccess(responseData: HomeResponse) {
        finalDataList.clear()
        finalDataList.addAll(responseData.data.venue)
        listOfData.clear()
        autoSuggestAdapter!!.notifyDataSetChanged()
        for (x in 0 until finalDataList.size) {
            if (responseData.data.venue[x].name.isNotEmpty()) {
                listOfData.add(x.toString() + "-" + responseData.data.venue[x].name)
            } else {
                listOfData.add("$x- ")
            }

        }

        autoSuggestAdapter!!.setData(listOfData);
        autoSuggestAdapter!!.notifyDataSetChanged()

    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map

        mMap!!.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context, R.raw.style_json
            )
        )
        setupPlaceAutoComplete(requireView())
        setupClickListener(requireView())
        setUpLanguage(requireView())
        setupGestures(view)
        setupSearchAutocomplete()

        val adapter = CustomInfoWindowAdapter(requireActivity())
        mMap!!.setInfoWindowAdapter(adapter)

        mMap!!.setOnInfoWindowClickListener {
            val venueData = it.getTag() as Venue
            if (venueData.type.equals("user", true)) {
                if (Utility.checkProfileComplete(sharedPreference)) {
                    val intent = Intent(activity, AProfileDetails::class.java)
                    intent.putExtra(ConstantLib.FRIEND_ID, venueData.id.toString())
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, venueData.image)
                    startActivity(intent)
                    Animatoo.animateSlideRight(activity)
                } else {
                    val dialog: BottomDialogExtended =
                        BottomDialogExtended.newInstance(
                            languageData!!.profilecompletedata,
                            arrayOf(languageData!!.yes)
                        )
                    dialog.show(childFragmentManager, "dialog")
                    dialog.setListener { position ->
                        val intent = Intent(
                            requireContext(),
                            AViewAndEditProfile::class.java
                        )
                        startActivity(intent)
                    }
                }

            } else {
                val intent = Intent(activity, AVenueDetails::class.java)
                intent.putExtra(ConstantLib.VENUE_ID, venueData.id.toString())
                intent.putExtra(ConstantLib.IS_GOOGLE_VENUE, venueData.is_google_venue.toString())
                startActivity(intent)
            }
        }

    }

}

interface onInfoWindowItemClicked{
    fun onItemClicked(venueData: Venue)
}
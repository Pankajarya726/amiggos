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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.EasyWayLocation.LOCATION_SETTING_REQUEST_CODE
import com.example.easywaylocation.Listener
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.JsonObject
import com.jakewharton.rxbinding2.widget.RxTextView
import com.kaopiz.kprogresshud.KProgressHUD
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.custom.BottomDialogExtended
import com.tekzee.amiggos.databinding.HomeFragmentBinding
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.homescreen_new.AHomeScreen
import com.tekzee.amiggos.ui.homescreen_new.CustomInfoWindowAdapter
import com.tekzee.amiggos.ui.homescreen_new.NotifyNotification
import com.tekzee.amiggos.ui.homescreen_new.homefragment.adapter.AutoSuggestAdapter
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse
import com.tekzee.amiggos.ui.homescreen_new.homefragment.model.HomeResponse.Data.Venue
import com.tekzee.amiggos.ui.homescreen_new.model.BadgeCountResponse
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
    /*Listener,*/ com.google.android.gms.maps.OnMapReadyCallback, Listener {

    private var progressDialog: KProgressHUD? = null
    private var binding: HomeFragmentBinding? = null
    private var mMap: GoogleMap? = null
    private lateinit var notifylistner: NotifyNotification
    private var finalDataList = ArrayList<Venue>()
    val listOfData = ArrayList<String>()
    private var autoSuggestAdapter: AutoSuggestAdapter? = null
    private var searchText: AutoCompleteTextView? = null
    private var placesApi: PlaceAPI? = null
    private var dataResponse = ArrayList<Venue>()
    private var longitude: String? = " 0.0"
    private var latitude: String? = "0.0"
    private lateinit var homepresenterImplementation: HomePresenterImplementation


    private var sharedPreference: SharedPreference? = null
    private var categoryId: String = ""
    private var searchkeyword: String = ""
    private var languageData: LanguageData? = null
    private var easyWayLocation: EasyWayLocation? = null
    private var defaultZoomValue =17.0f


    companion object {
        private val homefragment: HomeFragment? = null
        var staticRequestBadgeCount: Int = 0
        var staticReaFriendBadgeCount: Int = 0
        var staticNearMeBadgeCount: Int = 0

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        homepresenterImplementation = HomePresenterImplementation(this, requireContext())
        sharedPreference = SharedPreference(requireContext())
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        setupProgressBar()
        showProgressbarNew()
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding!!.root
    }

    private fun setupProgressBar() {
        progressDialog = KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(false)

    }

    private fun callBadgeApi() {
        val input = JsonObject()
        input.addProperty("userid", sharedPreference!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("latitude", latitude)
        input.addProperty("longitude", longitude)
        homepresenterImplementation.doCallBadgeApi(
            input,
            Utility.createHeaders(sharedPreference),
            languageData
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        easyWayLocation = EasyWayLocation(requireContext(), false, this)

    }

    private fun showProgressbarNew() {
        binding!!.headerlayout.visibility = View.GONE
        binding!!.maplayout.visibility = View.GONE
//        binding!!.homeprogressbar.visibility = View.VISIBLE
        progressDialog!!.show()
    }


    private fun setupGestures(view: View?) {

        requireView().findViewById<View>(R.id.viewleft).setOnTouchListener(
            object : OnSwipeTouchListener(activity) {
                override fun onSwipeLeft() {
                    Log.d("onSwipeLeft", "onSwipeLeft")
                }

                override fun onSwipeRight() {
                    val intent = Intent(activity, CameraActivity::class.java)
                    intent.putExtra(ConstantLib.FROM_ACTIVITY, ConstantLib.HOMEACTIVITY)
                    intent.putExtra(ConstantLib.OURSTORYID, "")
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

        view.findViewById<RadioButton>(R.id.img_icon_four).setOnClickListener {
            categoryId = "186"
            callHomeApi(0)
        }



        view.findViewById<RadioButton>(R.id.img_icon_five).setOnClickListener {
            categoryId = "194"
            callHomeApi(0)
        }


        view.findViewById<RadioButton>(R.id.img_icon_three).setOnClickListener {
            categoryId = "195"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_two).setOnClickListener {
            categoryId = "196"
            callHomeApi(0)
            setupRadioButton(R.id.img_icon_two)
        }


        view.findViewById<RadioButton>(R.id.img_icon_one).setOnClickListener {
            categoryId = "193"
            callHomeApi(0)
        }

        view.findViewById<RadioButton>(R.id.img_icon_six).setOnClickListener {
            categoryId = "197"
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
                    mMap!!.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                latitude!!.toDouble(),
                                longitude!!.toDouble()
                            )
                        )
                    )
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(defaultZoomValue));
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

    override fun onDetach() {
        super.onDetach()
        homepresenterImplementation.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgressbarNew()
    }

    private fun hideProgressbarNew() {
        binding!!.headerlayout.visibility = View.VISIBLE
        binding!!.maplayout.visibility = View.VISIBLE
        binding!!.homeprogressbar.visibility = View.GONE
        progressDialog!!.dismiss()
    }

    override fun onHomeApiSuccess(responseData: HomeResponse) {
        /*val cameraPosition =
            CameraPosition.Builder().target(LatLng(
                latitude!!.toDouble(),
                longitude!!.toDouble()
            )).tilt(30f).zoom(defaultZoomValue)
                .build()

        val cu: CameraUpdate = CameraUpdateFactory.newCameraPosition(
            cameraPosition
        )
        mMap!!.animateCamera(cu)*/

        if (dataResponse.isNotEmpty()) {
            mMap!!.clear()
            dataResponse.clear()
        }

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
            mMap!!.animateCamera(cu)
        }

    }

    private fun setMarkersOnMap(nearestClubs: java.util.ArrayList<Venue>) {
        for (items in nearestClubs) {
            var marker: View? = null
            var imageview: ImageView? = null
            val layoutInflater: LayoutInflater = LayoutInflater.from(context)
            if (items.type.equals("venue")) {
                marker = layoutInflater.inflate(R.layout.custom_marker_circular, null)
                imageview = marker.findViewById(R.id.marker_image) as ImageView

                val countnearby = marker.findViewById(R.id.countnearby) as TextView

                if (items.nearByCount.toInt() > 0) {
                    countnearby.text = items.nearByCount
                    countnearby.visibility = View.VISIBLE
                } else {
                    countnearby.visibility = View.GONE
                }


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


//        mMarkerImageView.setImageBitmap(bitmap)
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
//        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
//        view.buildDrawingCache()
//        val returnedBitmap = Bitmap.createBitmap(
//            view.measuredWidth, view.measuredHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(returnedBitmap)
//        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
//        val drawable = view.background
//        drawable?.draw(canvas)
//        view.draw(canvas)
//        return returnedBitmap
    }


    override fun onHomeApiFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onBadgeApiSuccess(responseData: BadgeCountResponse) {
        val bottomNearMeBatchCount =
            responseData.data.nearByCountBatch.toInt() + responseData.data.realFreind.toInt() + responseData.data.request.toInt()
        AHomeScreen.setupNearByCountBadge(bottomNearMeBatchCount)
        AHomeScreen.setupMemoryCountBadge(responseData.data.memoryCountBatch.toInt())
        AHomeScreen.setupBookingCountBadge(responseData.data.bookingCountBatch.toInt())
        AHomeScreen.setupBookingCountBadge(responseData.data.bookingCountBatch.toInt())
        staticRequestBadgeCount = responseData.data.request.toInt()
        staticReaFriendBadgeCount = responseData.data.realFreind.toInt()
        staticNearMeBadgeCount = responseData.data.nearByCountBatch.toInt()
        ConstantLib.NOTIFICATIONCOUNT = responseData.data.notificationCountBatch
        notifylistner.onNotify()
    }

    override fun validateError(message: String) {


        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun logoutUser() {
        Utility.showLogoutPopup(requireContext(), languageData!!.session_error)
    }


    @SuppressLint("CheckResult")
    private fun setupSearchAutocomplete() {
        searchText = requireView().findViewById<AutoCompleteTextView>(R.id.search_txt)

        RxTextView.textChanges(searchText!!).filter { it.length > 2 }
            .debounce(500, TimeUnit.MILLISECONDS).subscribeOn(
                Schedulers.io()
            ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.isEmpty()) {
                    requireView().findViewById<AutoCompleteTextView>(R.id.search_txt).hint =
                        languageData!!.pWhatWouldYouLikeToDo
                } else {
                    callApi(it.toString())
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
            val cu: CameraUpdate =
                CameraUpdateFactory.newLatLngBounds(latLngBounds, padding)
            mMap!!.moveCamera(cu)
        }
    }


//    override fun locationCancelled() {
//        hideProgressbarNew()
//        Logger.d("Location cancelled called")
//    }

//    override fun locationOn() {
//        Logger.d("Location on called")
//    }


//    override fun currentLocation(location: Location?) {
//        latitude = location!!.latitude.toString()
//        longitude = location.longitude.toString()
//        easyWayLocation!!.endUpdates()
//        hideProgressbarNew()
//        requireActivity().runOnUiThread(Runnable {
//            hideKeyboard()
//            mMap!!.moveCamera(
//                CameraUpdateFactory.newLatLng(
//                    LatLng(
//                        latitude!!.toDouble(),
//                        longitude!!.toDouble()
//                    )
//                )
//            )
//            mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
//            callHomeApi(0)
//        })
//        callBadgeApi()
//    }

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

//        mMap!!.isMyLocationEnabled = true
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
            val venueData = it.tag as Venue
            if (venueData.type.equals("user", true)) {
                if (Utility.checkProfileComplete(sharedPreference)) {
                    val intent = Intent(activity, AProfileDetails::class.java)
                    intent.putExtra(ConstantLib.FRIEND_ID, venueData.id.toString())
                    intent.putExtra(ConstantLib.PROFILE_IMAGE, venueData.image)
//                    startActivity(intent)
                    startActivityForResult(intent, 111)
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
//                        startActivity(intent)
                        startActivityForResult(intent, 111)
                    }
                }

            } else {
                if (Utility.checkProfileComplete(sharedPreference)) {
                    val intent = Intent(activity, AVenueDetails::class.java)
                    intent.putExtra(ConstantLib.VENUE_ID, venueData.id.toString())
                    intent.putExtra(ConstantLib.IS_GOOGLE_VENUE, venueData.is_google_venue.toInt())
//                startActivity(intent)
                    startActivityForResult(intent, 111)
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
//                        startActivity(intent)
                        startActivityForResult(intent, 111)
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTING_REQUEST_CODE -> easyWayLocation!!.onActivityResult(resultCode)
        }
        if (resultCode == 2) {
            searchkeyword = ""
            categoryId = ""
            binding!!.radiogroup.clearCheck()
            requireView().findViewById<AutoCompleteTextView>(R.id.autoCompleteEditText).setText("")
            requireView().findViewById<AutoCompleteTextView>(R.id.search_txt).setText("")
            callHomeApi(0)
        }
    }

    override fun locationOn() {
        Log.d("test", "location on")
    }

    override fun currentLocation(location: Location?) {
        try {
            if (location != null) {
                easyWayLocation!!.endUpdates()
                latitude = location.latitude.toString()
                longitude = location.longitude.toString()
                hideProgressbarNew()
                requireActivity().runOnUiThread(Runnable {
                    hideKeyboard()
                    mMap!!.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                latitude!!.toDouble(),
                                longitude!!.toDouble()
                            )
                        )
                    )
                    mMap!!.animateCamera(CameraUpdateFactory.zoomTo(defaultZoomValue))
                    callHomeApi(0)
                })
                callBadgeApi()
            } else {
                easyWayLocation!!.startLocation()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            hideProgressbarNew()
        }

        /*try {
            SmartLocation.with(requireActivity()).location().oneFix()
                .start { locationData ->
                    Log.e(
                        "location Data----->",
                        locationData.altitude.toString() + "---" + locationData.longitude
                    )
                    SmartLocation.with(requireActivity()).location().stop()
                    latitude = locationData!!.latitude.toString()
                    longitude = locationData.longitude.toString()
                    hideProgressbarNew()
                    requireActivity().runOnUiThread(Runnable {
                        hideKeyboard()
                        mMap!!.moveCamera(
                            CameraUpdateFactory.newLatLng(
                                LatLng(
                                    latitude!!.toDouble(),
                                    longitude!!.toDouble()
                                )
                            )
                        )
                        mMap!!.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
                        callHomeApi(0)
                    })
                    callBadgeApi()
                }
        }catch (e: Exception){
            e.printStackTrace()
        }*/
    }

    override fun locationCancelled() {
        Log.d("test", "current location cancelled")
    }

    override fun onResume() {
        super.onResume()
        easyWayLocation!!.startLocation()
    }


}

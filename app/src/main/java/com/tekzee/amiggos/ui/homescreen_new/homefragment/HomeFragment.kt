package com.tekzee.amiggos.ui.homescreen_new.homefragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
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
import com.tekzee.mallortaxi.base.BaseFragment


class HomeFragment: BaseFragment(), OnMapReadyCallback, PermissionsListener {

    private lateinit var mstyle: Style
    private lateinit var mmapboxMap: MapboxMap
    private var mapView: MapView? = null
    private var  permissionsManager:PermissionsManager? =null
    private var img_my_location: ImageView? =null

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Mapbox.getInstance(this.context!!,resources.getString(R.string.mapbox_token))

        val view = inflater.inflate(R.layout.home_fragment, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        setupViews(view)

        return view;
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

    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        mmapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS, fun(style: Style) {
            mstyle = style
            enableLocationComponent(style)


            val symbolManager = SymbolManager(mapView!!, mapboxMap, style)
            symbolManager.iconAllowOverlap = true
            symbolManager.iconIgnorePlacement = true
            // Add symbol at specified lat/lon

            val bm: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.a_notification)
            style.addImage("my-marker", bm)

            symbolManager.create(
                SymbolOptions()
                .withLatLng(LatLng(60.169091, 24.939876))
                .withIconImage("my-marker")
                    .withTextField("Himanshu")
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withIconSize(1.0f))



            symbolManager.addClickListener { symbol: Symbol? ->  Toast.makeText(activity,"clicked  " + symbol!!.getTextField().toLowerCase().toString(),Toast.LENGTH_SHORT).show();
            }
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
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(activity!!, style).build())

            // Enable to make component visible
            locationComponent.isLocationComponentEnabled = true

            // Set the component's camera mode
            locationComponent.cameraMode = CameraMode.TRACKING

            // Set the component's render mode
            locationComponent.renderMode = RenderMode.COMPASS

            locationComponent.isLocationComponentEnabled = true


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(activity)
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(activity, "user_location_permission_explanation", Toast.LENGTH_LONG).show();
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            mmapboxMap.getStyle { style -> enableLocationComponent(style)}
        }else{
            Toast.makeText(activity, "user_location_permission_not_granted", Toast.LENGTH_LONG).show();
            activity!!.finish()
        }
    }



}
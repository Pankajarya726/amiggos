package com.tekzee.amiggoss.util


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task

class InitGeoLocationUpdate {
    companion object {
        private var locationCallBack: LocationCallback? = null
        private var fusedLocationClient: FusedLocationProviderClient? = null
        private lateinit var locationLatLngCallback : SimpleCallback<LatLng>;

        fun locationInit(context: Context, locationLatLngCallback: SimpleCallback<LatLng>) {
            Companion.locationLatLngCallback = locationLatLngCallback
            createLocationRequest(context, getLocationCallBack());
        }

        private fun getLocationCallBack(): LocationCallback {
            if (locationCallBack == null)
                return initLocationCallback()
            return locationCallBack!!
        }


        private fun initLocationCallback(): LocationCallback {
            locationCallBack = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)

                    val mCurrentLocation = locationResult!!.lastLocation
                    val mCurrentLatLng = LatLng(mCurrentLocation!!.latitude, mCurrentLocation.longitude)

                    locationLatLngCallback.callback(mCurrentLatLng)

                    Log.d("TAG", "Current Location Latitude: " + mCurrentLatLng.latitude + " Longitude: " +
                            mCurrentLatLng.longitude)
                }
            }
            return locationCallBack!!
        }

        @SuppressLint("RestrictedApi")
        private fun createLocationRequest(context: Context, locationCallback: LocationCallback) {
            val locationRequest = LocationRequest().apply {
                interval = 1000
                fastestInterval = 2000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            buildLocationSettingsRequest(context, locationRequest, locationCallback);
        }

        private fun getFusedLocationClient(context: Context): FusedLocationProviderClient? {
            if (fusedLocationClient == null)
                return createFusedLocationClient(context)

            return fusedLocationClient
        }

        private fun createFusedLocationClient(context: Context): FusedLocationProviderClient? {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            return fusedLocationClient
        }

        private val REQUEST_CHECK_SETTINGS = 0x1
        private fun buildLocationSettingsRequest(context: Context, locationRequest: LocationRequest,
                                                 locationCallback: LocationCallback) {

            val builder = LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest).setAlwaysShow(true);

            val client: SettingsClient = LocationServices.getSettingsClient(context)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())


            task.addOnSuccessListener(context as Activity) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    getFusedLocationClient(context)!!.requestLocationUpdates(locationRequest,
                        locationCallback, Looper.myLooper());
            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        it.startResolutionForResult(context as Activity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        }

        fun stopLocationUpdate(context: Context) {
            getFusedLocationClient(context as Activity)!!.removeLocationUpdates(getLocationCallBack())
        }
    }
}
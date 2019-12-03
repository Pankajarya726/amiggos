package com.tekzee.amiggos.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.IBinder
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.enums.Actions
import com.tekzee.amiggos.ui.home.HomeActivity
import com.tekzee.amiggos.ui.invitefriend.InitGeoLocationUpdate
import com.tekzee.mallortaxi.network.ApiClient
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxi.util.SimpleCallback
import com.tekzee.mallortaxi.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*

class UpdateUserLocationToServer : Service() {
    private var activity: Activity?= null
    private var city: String? = ""
    private var state: String? =""
    private var postalCode: String? = ""
    private var countryName: String? = ""
    private var lastLocation: LatLng? = null
    private var disposable: Disposable? = null
    private var sharedPreferences: SharedPreference? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        if (intent != null) {
            val action = intent.action
            when (action) {
                Actions.START.name -> startService()
//                Actions.STOP.name -> stopService()
                else -> Logger.d("This should never happen. No action in the received intent")
            }
        } else {
            Logger.d(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        return START_STICKY
    }

    fun startService(){

        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
//        input.addProperty("latitude", lastLocation!!.latitude.toString())
//        input.addProperty("longitude", lastLocation!!.longitude.toString())
        input.addProperty("latitude", "22.7533")
        input.addProperty("longitude", "75.8937")
        input.addProperty("country", countryName)
        input.addProperty("state", state)
        input.addProperty("city", city)
        input.addProperty("postal_code", postalCode)





        disposable = ApiClient.instance.doUploadUserLocation(
            input,
            Utility.createHeaders(sharedPreferences)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                val responseCode = response.code()
                when (responseCode) {
                    200 -> {
                        val responseData: CommonResponse? = response.body()
                        if (responseData!!.status) {
                            //            mainView.onAttachIdSuccess(responseData)
                        } else {
                            //          mainView.validateError(responseData.message.toString())
                        }
                    }
                }
            }, { error ->
                // mainView.hideProgressbar()
                // mainView.validateError(error.message.toString())
            })
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = SharedPreference(this)
        //startLocationUpdate()
        val notification = createNotification()
        startForeground(1, notification)
    }


    private fun startLocationUpdate() {

        InitGeoLocationUpdate.locationInit(applicationContext, object : SimpleCallback<LatLng> {
            override fun callback(mCurrentLatLng: LatLng) {
                lastLocation = mCurrentLatLng
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("The service has been destroyed".toUpperCase())
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, HomeActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

    fun getAddressFromLocation(){
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()
        try {
            addresses = geocoder.getFromLocation(
                lastLocation!!.latitude,
                lastLocation!!.longitude,
                // In this sample, we get just a single address.
                1)
        } catch (ioException: IOException) {
            // Catch network or other I/O problems.

        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.

        }

        // Handle case where no address was found.
        if (addresses.isEmpty()) {
            Logger.d("Address Not found")
        } else {
            val address = addresses[0]
            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            val addressFragments = with(address) {
                (0..maxAddressLineIndex).map { getAddressLine(it) }
            }
            Logger.d("Address found")
            countryName = address.countryName
            state = ""
            city = ""
            postalCode = address.postalCode
        }
    }

}
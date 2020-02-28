package com.tekzee.mallortaxi.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.orhanobut.logger.Logger
import com.tekzee.amiggos.util.SharedPreference
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger


class ApplicationController: Application() {

    private var sharedPreference: SharedPreference? = null
    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this);
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        Logger.d("called logged out")
    }

}
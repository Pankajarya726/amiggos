package com.tekzee.mallortaxi.base

import android.app.Application
import com.orhanobut.logger.Logger
import com.tekzee.mallortaxi.util.SharedPreference
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


    override fun onTerminate() {
        super.onTerminate()
        Logger.d("called logged out")
    }

}
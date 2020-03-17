package com.tekzee.mallortaxi.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.orhanobut.logger.Logger
import com.stripe.android.PaymentConfiguration


class ApplicationController: Application() {


    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this);
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_E5sEgimoA8T4SexAo1GnNPkJ00sG6jBcdG"
        )



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
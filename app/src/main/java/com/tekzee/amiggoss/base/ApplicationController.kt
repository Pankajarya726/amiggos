package com.tekzee.amiggoss.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.devs.acr.AutoErrorReporter
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
            getApplicationContext(),
            "pk_test_E5sEgimoA8T4SexAo1GnNPkJ00sG6jBcdG"
        );

        AutoErrorReporter.get(this)
            .setEmailAddresses("himanshu.verma@tekzee.com")
            .setEmailSubject("Auto Crash Report")
            .start()

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
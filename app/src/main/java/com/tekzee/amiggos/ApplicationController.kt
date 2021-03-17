package com.tekzee.amiggos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDex
import com.devs.acr.AutoErrorReporter

import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.stripe.android.PaymentConfiguration
import com.takwolf.android.foreback.Foreback
import com.tekzee.amiggos.base.model.CommonResponse
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.*
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.network.ApiClient
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.ui.chatnew.ChatViewModelFactory
import com.tekzee.amiggos.ui.message.MessagesViewModelFactory
import com.tekzee.amiggos.ui.storieviewnew.StorieViewModelFactory
import com.tekzee.amiggos.ui.tagging.TaggingViewModelFactory
import com.tekzee.amiggos.ui.taggingvideo.TaggingVideoViewModelFactory
import com.tekzee.amiggos.util.NetworkConnectionInterceptor
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.addusers.AddUserViewModelFactory
import com.tekzee.amiggos.ui.bookingdetailnew.model.BookingDetailsNewResponse
import com.tekzee.amiggos.ui.finalbasket.FinalBasketViewModelFactory
import com.tekzee.amiggos.ui.invitationlist.InvitationListViewModelFactory
import com.tekzee.amiggos.ui.memories.mymemories.MyMemorieViewModelFactory
import com.tekzee.amiggos.ui.menu.MenuModelFactory
import com.tekzee.amiggos.ui.menu.commonfragment.CommonFragmentViewModelFactory
import com.tekzee.amiggos.util.Utility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class ApplicationController : Application(), KodeinAware, Foreback.Listener {

    var sharedPreferences: SharedPreference? = null

    companion object {
        var simpleCache: SimpleCache? = null
    }


    override val kodein: Kodein = Kodein.lazy {

        import(androidXModule(this@ApplicationController))
        bind() from singleton { FirebaseAuth.getInstance() }
        bind() from singleton { FirebaseDatabase.getInstance() }
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { SharedPreference(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { applicationContext }
        bind() from singleton { LanguageData() }
        bind() from singleton { FirebaseDatabase.getInstance().reference }
        bind() from singleton { CompositeDisposable() }


        //bind repository

        bind() from singleton { TaggingRepository(instance()) }
        bind() from singleton { MemorieRepository(instance()) }
        bind() from singleton { StorieRepository(instance()) }
        bind() from singleton { NotificationRepository(instance()) }
        bind() from singleton { ChatRepository(instance(), instance()) }
        bind() from singleton { AddUserRepository(instance()) }
        bind() from singleton { MemorieFeaturedBrandRepository(instance()) }
        bind() from singleton { MenuRepository(instance()) }
        bind() from singleton { FinalBasketRepository(instance()) }
        bind() from singleton { InvitationListRepository(instance()) }


//        bind() from singleton { ValidateAppVersionRespository(instance()) }
//        bind() from singleton { LoginRepository(instance()) }
//        bind() from singleton { ForgetPasswordRepository(instance()) }
//        bind() from singleton { SettingsRepository(instance()) }
//        bind() from singleton { BookingRepository(instance()) }
//        bind() from singleton { BlockedUserRepository(instance()) }
//        bind() from singleton { LanguageRepository(instance()) }
//        bind() from singleton { LocationRepository(instance()) }
//        bind() from singleton { BrandRepository(instance()) }
//        bind() from singleton { SettingsMemorieRepository(instance()) }
//        bind() from singleton { CityRepository(instance()) }


//        bind() from singleton { MemorieFeaturedBrandRepository(instance()) }
//        bind() from singleton { ApprovalRepository(instance()) }
//        bind() from singleton { DashboardRepository(instance()) }
//
//
//        bind() from singleton { StaffRepository(instance()) }
//        bind() from singleton { StripePaymentRepository(instance()) }
//        bind() from singleton { PromotersRepository(instance()) }
//        bind() from singleton { AddUserRepository(instance()) }

//        bind() from singleton { HomeRepository(instance()) }


        //binding viewmodel

        bind() from singleton {
            TaggingViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            TaggingVideoViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            StorieViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            ChatViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            MessagesViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            AddUserViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            MyMemorieViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            MenuModelFactory(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            CommonFragmentViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            FinalBasketViewModelFactory(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind() from singleton {
            InvitationListViewModelFactory(
                instance(),
                instance(),
                instance()
            )
        }
    }


    override fun onCreate() {
        super.onCreate()
        Foreback.init(this)
        Foreback.registerListener(this)
        sharedPreferences = SharedPreference(applicationContext)
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> System.out.println("Exception" + throwable!!.localizedMessage) }
//        AppEventsLogger.activateApp(this);
//        FacebookSdk.setIsDebugEnabled(true)
//        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        PaymentConfiguration.init(
            getApplicationContext(),
            "pk_test_E5sEgimoA8T4SexAo1GnNPkJ00sG6jBcdG"
        );

        AutoErrorReporter.get(this)
            .setEmailAddresses("himanshu.verma@tekzee.com")
            .setEmailSubject("Auto Crash Report")
            .start()


        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024)
        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)

        if (simpleCache == null) {
            simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, databaseProvider)
        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun onTerminate() {
        Toast.makeText(applicationContext, "onTerminate", Toast.LENGTH_LONG).show()
        super.onTerminate()

    }

    override fun onApplicationEnterForeground(activity: Activity?) {
        callUpdateTimeApi("1")
    }

    override fun onApplicationEnterBackground(activity: Activity?) {
        callUpdateTimeApi("2")
        sharedPreferences!!.save(ConstantLib.CALLBATCHCOUNT,true)
    }

    @SuppressLint("CheckResult")
    fun callUpdateTimeApi(actiontime: String) {
        val input = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("actiontype", actiontime)
        ApiClient.instance.doUpdateAppTimeApi(input, Utility.createHeaders(sharedPreferences))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                when (response.code()) {
                    200 -> {
                        val responseData: CommonResponse? = response.body()
                        if (responseData!!.status) {
                            Log.e("successfully send---->", "Uptime")
                        } else {
                            Log.e("error occured in ---->", "Uptime")
                        }
                    }
                    404 -> {
                        Log.e("error occured---->", "Logout")
                    }
                }
            }, { error ->
                Log.e("error occured---->", error.message)
            })

    }
}
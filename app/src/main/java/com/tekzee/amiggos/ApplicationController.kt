package com.tekzee.amiggos

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.devs.acr.AutoErrorReporter
import com.facebook.FacebookSdk
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.orhanobut.logger.Logger
import com.stripe.android.PaymentConfiguration
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.*
import com.tekzee.amiggos.network.ApiService
import com.tekzee.amiggos.room.dao.ItemDao
import com.tekzee.amiggos.ui.chatnew.ChatViewModelFactory
import com.tekzee.amiggos.ui.message.MessagesViewModelFactory
import com.tekzee.amiggos.ui.storieviewnew.StorieViewModelFactory
import com.tekzee.amiggos.ui.tagging.TaggingViewModelFactory
import com.tekzee.amiggos.ui.taggingvideo.TaggingVideoViewModelFactory
import com.tekzee.amiggos.util.NetworkConnectionInterceptor
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.ui.addusers.AddUserViewModelFactory
import com.tekzee.amiggos.ui.finalbasket.FinalBasketViewModelFactory
import com.tekzee.amiggos.ui.memories.mymemories.MyMemorieViewModelFactory
import com.tekzee.amiggos.ui.menu.MenuModelFactory
import com.tekzee.amiggos.ui.menu.commonfragment.CommonFragmentViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class ApplicationController : Application(), KodeinAware {

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


//        bind() from singleton {
//            LoginViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            VenueDashboardViewModelFactory(
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            BrandDashboardViewModelFactory(
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton { SplashViewModelFactory(instance(), instance()) }
//        bind() from singleton { ForgetPasswordFactory(instance(), instance(), instance()) }
//        bind() from singleton {
//            SettingsViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            PromotersViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            AddUserViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            BookingDetailsViewModelFactory(
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            StaffViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            CommonFragmentViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//
//
//        bind() from singleton {
//            CommonViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            MyMemorieViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            DashboardCommonViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            ApprovalViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//

//
//        bind() from singleton {
//            ChatViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//

//        bind() from singleton {
//            CityViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            SettingsMemorieViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            LocationViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//        bind() from singleton {
//            BrandViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            BlockedViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            LanguageViewModelFactory(
//                instance(),
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            StripeViewModelFactory(
//                instance(),
//                instance(),
//                instance()
//            )
//        }
//
//        bind() from singleton {
//            AddCardModelFactory(
//                instance(),
//                instance(),
//                instance()
//            )
//        }
    }


    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler { throwable: Throwable? -> System.out.println("Exception" + throwable!!.localizedMessage) }
        AppEventsLogger.activateApp(this);
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
        PaymentConfiguration.init(
            getApplicationContext(),
            "pk_test_E5sEgimoA8T4SexAo1GnNPkJ00sG6jBcdG"
        );

//        AutoErrorReporter.get(this)
//            .setEmailAddresses("himanshu.verma@tekzee.com")
//            .setEmailSubject("Auto Crash Report")
//            .start()


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
        super.onTerminate()
        Logger.d("called logged out")
    }

}
package com.tekzee.amiggos.ui.homescreen_new

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.cameranew.CameraActivity
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.AHomeScreenBinding
import com.tekzee.amiggos.enums.Actions
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.services.UpdateUserLocationToServer
import com.tekzee.amiggos.ui.bookings_new.BookingFragment
import com.tekzee.amiggos.ui.chat.myfriendchatlist.MyFriendChatActivity
import com.tekzee.amiggos.ui.homescreen_new.homefragment.HomeFragment
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.NearMeFragment
import com.tekzee.amiggos.ui.memories.AMemoriesFragment
import com.tekzee.amiggos.ui.mylifestyle.AMyLifeStyle
import com.tekzee.amiggos.ui.notification_new.ANotification
import com.tekzee.amiggos.ui.settings_new.ASettings
import com.tekzee.amiggos.util.SharedPreference


class AHomeScreen : BaseActivity(), AHomeScreenPresenter.AHomeScreenMainView,
    BottomNavigationView.OnNavigationItemSelectedListener,NotifyNotification {

    var binding: AHomeScreenBinding?= null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null


    companion object {
        var bottomNavigation: BottomNavigationView? = null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_home_screen)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        bottomNavigation = binding!!.bottomNavigation
        bottomNavigation!!.setOnNavigationItemSelectedListener(this)



        setupLanguage()
        setupClickListener()

        //check permissions
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {

            if (intent.action == FriendsAction.CLICK.action) {
                bottomNavigation!!.menu.getItem(1).isChecked = true
                setHeaders(R.id.navigation_near_me)
                openFragment(NearMeFragment.newInstance(intent), "2")
            } else  if (intent.action == "BOOKING") {
                bottomNavigation!!.menu.getItem(4).isChecked = true
                setHeaders(R.id.navigation_bookings)
                openFragment(BookingFragment.newInstance(intent), "2")
            }else {
                setHeaders(R.id.navigation_home)
                openFragment(HomeFragment.newInstance(), "1")

            }


        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }

            if (e.hasForeverDenied()) {
                e.goToSettings()
            }
        }

        Intent(this, UpdateUserLocationToServer::class.java).also { intent ->
            intent.action = Actions.START.name
            startService(intent)
        }

//        try {
//            if(intent.getStringExtra(ConstantLib.FROM).equals("FRIENDNOTIFICATION")){
//                setHeaders(R.id.navigation_near_me)
//                openFragment(NearMeFragment.newInstance(intent), "2")
//            }else{
//
//            }
//        }catch (e:Exception){
//            e.printStackTrace()
//        }


    }

    override fun onStart() {
        super.onStart()

    }


    private fun setupLanguage() {

        bottomNavigation!!.menu.getItem(0).title = languageData!!.PHome
        bottomNavigation!!.menu.getItem(1).title = languageData!!.PNearme
        bottomNavigation!!.menu.getItem(2).title = languageData!!.PMylifestyle
        bottomNavigation!!.menu.getItem(3).title = languageData!!.PMemories
        bottomNavigation!!.menu.getItem(4).title = languageData!!.PBooking

    }


    private fun setupClickListener() {
        binding!!.menuDrawer.setOnClickListener {
            val intent = Intent(this@AHomeScreen, ASettings::class.java)
            startActivity(intent)
            Animatoo.animateSlideRight(this)
        }

        binding!!.addMemorie.setOnClickListener {
            val intent = Intent(applicationContext, CameraActivity::class.java)
            intent.putExtra(ConstantLib.FROM_ACTIVITY, "HOMEACTIVITY")
            intent.putExtra(
                ConstantLib.PROFILE_IMAGE,
                sharedPreference!!.getValueString(ConstantLib.PROFILE_IMAGE)
            )
            startActivity(intent)
        }

        binding!!.notification.setOnClickListener {
            val intent = Intent(this, ANotification::class.java)
            startActivity(intent)
        }

        binding!!.chaticon.setOnClickListener {
            val intent = Intent(applicationContext, MyFriendChatActivity::class.java)
            startActivity(intent)
        }

        binding!!.checkincode.setOnClickListener {
//            val intent = Intent(this, MyBookingActivity::class.java)
//            startActivity(intent)
        }
    }

    @SuppressLint("LongLogTag")
    private fun openFragment(fragment: Fragment, fragmentName: String) {
        Handler().postDelayed({
            val transaction = supportFragmentManager.beginTransaction().apply {
                replace(R.id.container, fragment, fragmentName)
            }
            transaction.commit()
        }, 200)
    }

    override fun validateError(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                setHeaders(R.id.navigation_home)
                openFragment(HomeFragment.newInstance(), "1")
                return true
            }
            R.id.navigation_near_me -> {
                setHeaders(R.id.navigation_near_me)
                openFragment(NearMeFragment.newInstance(intent), "2")
                return true
            }
            R.id.navigation_my_lifestyle -> {
                setHeaders(R.id.navigation_my_lifestyle)
                openFragment(AMyLifeStyle.newInstance(), "3")
                return true
            }
            R.id.navigation_memories -> {
                setHeaders(R.id.navigation_memories)
                openFragment(AMemoriesFragment.newInstance(), "4")
                return true
            }
            R.id.navigation_bookings -> {
                setHeaders(R.id.navigation_bookings)
                openFragment(BookingFragment.newInstance(intent), "5")
                return true
            }
        }
        return false
    }

    private fun setHeaders(navigationMemories: Int) {



        if (navigationMemories == R.id.navigation_memories) {
            binding!!.headerLogo.visibility = View.GONE
            binding!!.notification.visibility = View.GONE
            binding!!.badge.visibility = View.GONE
            binding!!.addMemorie.visibility = View.VISIBLE
            binding!!.chaticon.visibility = View.GONE
            binding!!.checkincode.visibility = View.GONE
        } else if (navigationMemories == R.id.navigation_home) {
            binding!!.badge.visibility = View.VISIBLE
            binding!!.badge.setNumber(Integer.parseInt(ConstantLib.NOTIFICATIONCOUNT))
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
            binding!!.checkincode.visibility = View.GONE
        } else if (navigationMemories == R.id.navigation_bookings) {
            binding!!.badge.visibility = View.VISIBLE
            binding!!.badge.setNumber(Integer.parseInt(ConstantLib.NOTIFICATIONCOUNT))
            binding!!.headerLogo.visibility = View.GONE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
        } else if (navigationMemories == R.id.navigation_near_me) {
            binding!!.headerLogo.visibility = View.GONE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.GONE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.VISIBLE
        } else if (navigationMemories == R.id.navigation_my_lifestyle) {
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
        }
    }


    override fun onBackPressed() {
        if (bottomNavigation!!.menu.getItem(0).isChecked) {
            onBackPressed()
        } else {
            bottomNavigation!!.menu.getItem(0).isChecked = true
            openFragment(HomeFragment.newInstance(), "1")

        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onNotify() {
        binding!!.badge.setNumber(Integer.parseInt(ConstantLib.NOTIFICATIONCOUNT))
    }


}
package com.tekzee.amiggos.ui.homescreen_new

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.allenliu.badgeview.BadgeFactory
import com.allenliu.badgeview.BadgeView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.AHomeScreenBinding
import com.tekzee.amiggos.enums.Actions
import com.tekzee.amiggos.enums.FriendsAction
import com.tekzee.amiggos.services.UpdateUserLocationToServer
import com.tekzee.amiggos.ui.bookings_new.BookingFragment
import com.tekzee.amiggos.ui.cameranew.CameraActivity
import com.tekzee.amiggos.ui.homescreen_new.homefragment.HomeFragment
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.NearMeFragment
import com.tekzee.amiggos.ui.memories.AMemoriesFragment
import com.tekzee.amiggos.ui.message.MessageActivity
import com.tekzee.amiggos.ui.message.model.Message
import com.tekzee.amiggos.ui.mylifestyle.AMyLifeStyle
import com.tekzee.amiggos.ui.notification_new.ANotification
import com.tekzee.amiggos.ui.settings_new.ASettings
import com.tekzee.amiggos.ui.viewandeditprofile.AViewAndEditProfile
import com.tekzee.amiggos.util.SharedPreference


class AHomeScreen : BaseActivity(), AHomeScreenPresenter.AHomeScreenMainView,
    BottomNavigationView.OnNavigationItemSelectedListener, NotifyNotification {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private var badgeViewChat: BadgeView? = null
    var binding: AHomeScreenBinding? = null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var currentUser = firebaseAuth.currentUser!!.uid

    companion object {
        var bottomNavigation: BottomNavigationView? = null

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_home_screen)
        setupBadge()
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
                setHeaders(R.id.navigation_bookings)
                openFragment(BookingFragment.newInstance(intent, 2), "5")
            } else if (intent.action == FriendsAction.SHOW_FRIENDS.action) {
                bottomNavigation!!.menu.getItem(4).isChecked = true
                setHeaders(R.id.navigation_near_me)
                openFragment(NearMeFragment.newInstance(intent, 1), "2")
            } else if (intent.action == FriendsAction.SHOW_FRIEND_REQUEST.action) {
                bottomNavigation!!.menu.getItem(4).isChecked = true
                setHeaders(R.id.navigation_near_me)
                openFragment(NearMeFragment.newInstance(intent, 2), "2")
            } else if (intent.action == FriendsAction.PARTY_INVITATIONS.action) {
                bottomNavigation!!.menu.getItem(4).isChecked = true
                setHeaders(R.id.navigation_bookings)
                openFragment(BookingFragment.newInstance(intent, 1), "5")
            } else if (intent.action == FriendsAction.SHOW_MY_MEMORY.action) {
                bottomNavigation!!.menu.getItem(3).isChecked = true
                setHeaders(R.id.navigation_memories)
                openFragment(AMemoriesFragment.newInstance(), "4")
            } else {
                setHeaders(R.id.navigation_home)
                openFragment(HomeFragment.newInstance(), "1")

            }


        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage(languageData!!.locationpermission)
                    .setPositiveButton(languageData!!.yes) { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton(languageData!!.no) { dialog, which ->
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

    private fun setupBadge() {
        badgeViewChat = BadgeFactory.create(this)
            .setTextColor(resources.getColor(R.color.white))
            .setWidthAndHeight(20, 20)
            .setBadgeBackground(resources.getColor(R.color.red))
            .setTextSize(8)
            .setBadgeGravity(Gravity.RIGHT)
            .setBadgeCount(0)
            .setShape(BadgeView.SHAPE_CIRCLE)
            .setSpace(10, 10)
            .bind(binding!!.chaticon)


        getAllUnreadChatCount().observe(this, Observer {
            badgeViewChat!!.setBadgeCount(it.size)
        })

    }

    override fun onStart() {
        super.onStart()

    }


    private fun setupLanguage() {

        bottomNavigation!!.menu.getItem(0).title = languageData!!.pHome
        bottomNavigation!!.menu.getItem(1).title = languageData!!.pNearme
        bottomNavigation!!.menu.getItem(2).title = languageData!!.pMylifestyle
        bottomNavigation!!.menu.getItem(3).title = languageData!!.pMemories
        bottomNavigation!!.menu.getItem(4).title = languageData!!.pBooking

    }


    private fun setupClickListener() {
        binding!!.menuDrawer.setOnClickListener {
            if (sharedPreference!!.getValueInt(ConstantLib.ISPROFILECOMPLETE) == 1) {
                val intent = Intent(this@AHomeScreen, ASettings::class.java)
                startActivity(intent)
                Animatoo.animateSlideRight(this)
            } else {
                val intent = Intent(this, AViewAndEditProfile::class.java)
                intent.putExtra(ConstantLib.FROM, "EDIT")
                intent.putExtra(ConstantLib.NAME, sharedPreference!!.getValueString(ConstantLib.NAME))
                startActivity(intent)
                Animatoo.animateSlideRight(this)
            }


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
            val intent = Intent(applicationContext, MessageActivity::class.java)
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
                openFragment(NearMeFragment.newInstance(intent, 0), "2")
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
                openFragment(BookingFragment.newInstance(intent, 0), "5")
                return true
            }
        }
        return false
    }

    private fun setHeaders(navigationMemories: Int) {


        if (navigationMemories == R.id.navigation_memories) {
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.notification.visibility = View.GONE
            binding!!.badge.visibility = View.GONE
            binding!!.addMemorie.visibility = View.VISIBLE
            binding!!.chaticon.visibility = View.GONE
            badgeViewChat!!.unbind()
            binding!!.checkincode.visibility = View.GONE
        } else if (navigationMemories == R.id.navigation_home) {
            binding!!.badge.visibility = View.VISIBLE
            binding!!.badge.setNumber(Integer.parseInt(ConstantLib.NOTIFICATIONCOUNT))
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
            badgeViewChat!!.unbind()
            binding!!.checkincode.visibility = View.GONE
        } else if (navigationMemories == R.id.navigation_bookings) {
            binding!!.badge.visibility = View.VISIBLE
            binding!!.badge.setNumber(Integer.parseInt(ConstantLib.NOTIFICATIONCOUNT))
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
            badgeViewChat!!.unbind()
        } else if (navigationMemories == R.id.navigation_near_me) {
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.GONE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.VISIBLE
            badgeViewChat!!.bind(binding!!.chaticon)
        } else if (navigationMemories == R.id.navigation_my_lifestyle) {
            binding!!.headerLogo.visibility = View.VISIBLE
            binding!!.badge.visibility = View.GONE
            binding!!.notification.visibility = View.VISIBLE
            binding!!.checkincode.visibility = View.GONE
            binding!!.addMemorie.visibility = View.GONE
            binding!!.chaticon.visibility = View.GONE
            badgeViewChat!!.unbind()
        }
    }


    override fun onBackPressed() {
        if (bottomNavigation!!.menu.getItem(0).isChecked) {
            System.exit(0)
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

    private val unreadMessageList = MutableLiveData<List<Message>>()
    fun getAllUnreadChatCount(): MutableLiveData<List<Message>> {
        val listOfUnreadMessageCount = ArrayList<Message>()
        firebaseDatabase.reference.child(ConstantLib.MESSAGE).addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error message", databaseError.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfUnreadMessageCount.clear()
                    for (messages in snapshot.children) {
                        val messageData = messages.getValue(Message::class.java)
                        if (messageData != null) {
                            if (messageData.receiver.equals(
                                    currentUser,
                                    true
                                ) && !messageData.isSeen
                            ) {
                                listOfUnreadMessageCount.add(
                                    messageData
                                )
                            }
                        }
                    }
                    unreadMessageList.value = listOfUnreadMessageCount
                }
            })

        return unreadMessageList
    }
}
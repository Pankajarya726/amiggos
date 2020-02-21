package com.tekzee.amiggos.ui.homescreen_new

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.AHomeScreenBinding
import com.tekzee.amiggos.ui.homescreen_new.homefragment.HomeFragment
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.NearMeFragment
import com.tekzee.amiggos.ui.settings_new.ASettings
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.mallortaxi.util.SharedPreference
import com.tekzee.mallortaxiclient.constant.ConstantLib


class AHomeScreen : BaseActivity(), AHomeScreenPresenter.AHomeScreenMainView,BottomNavigationView.OnNavigationItemSelectedListener  {

    lateinit var binding: AHomeScreenBinding
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var bottomNavigation: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_home_screen)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        bottomNavigation = binding.bottomNavigation
        bottomNavigation!!.setOnNavigationItemSelectedListener(this)
        openFragment(HomeFragment.newInstance())
        setupClickListener()
    }

    private fun setupClickListener() {
        binding.menuDrawer.setOnClickListener {
            val intent = Intent(this@AHomeScreen,ASettings::class.java)
            startActivity(intent)
            Animatoo.animateSlideRight(this)
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }


    override fun validateError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                openFragment(HomeFragment.newInstance())
                return true
            }
            R.id.navigation_near_me -> {
                openFragment(NearMeFragment.newInstance())
                return true
            }
            R.id.navigation_my_lifestyle -> {
                openFragment(HomeFragment.newInstance())
                return true
            }
            R.id.navigation_memories -> {
                openFragment(HomeFragment.newInstance())
                return true
            }
            R.id.navigation_bookings -> {
                openFragment(HomeFragment.newInstance())
                return true
            }
        }
        return false
    }


}
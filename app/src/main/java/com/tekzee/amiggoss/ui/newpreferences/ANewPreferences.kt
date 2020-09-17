package com.tekzee.amiggoss.ui.newpreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggoss.R
import com.tekzee.amiggoss.base.BaseActivity
import com.tekzee.amiggoss.base.model.LanguageData
import com.tekzee.amiggoss.databinding.NewPreferencesFragmentBinding
import com.tekzee.amiggoss.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggoss.ui.newpreferences.amusictypefragment.AMusicTypeFragment
import com.tekzee.amiggoss.ui.newpreferences.avenuetypefragment.AVenueTypeFragment
import com.tekzee.amiggoss.util.SharedPreference
import com.tekzee.amiggoss.constant.ConstantLib


class ANewPreferences : BaseActivity() {
    private var binding: NewPreferencesFragmentBinding? =null
    private var sharedPreference: SharedPreference? = null
    private var languageData: LanguageData? = null
    companion object {

        private val aNewPreferences: ANewPreferences? = null

        fun newInstance(): ANewPreferences{
            if(aNewPreferences == null){
                return ANewPreferences()
            }
            return aNewPreferences
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.new_preferences_fragment)
        sharedPreference = SharedPreference(this)
        languageData = sharedPreference!!.getLanguageData(ConstantLib.LANGUAGE_DATA)

        setupViews()
        setupLanguage()
    }


    private fun setupLanguage() {
        findViewById<TextView>(R.id.heading).text = languageData!!.PCHOOSEYOURPREFERENCES

    }

    private fun setupAdapter(
        viewPager: ViewPager,
        tabs: TabLayout
    ) {
        val fragmentManager = supportFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(AVenueTypeFragment.newInstance(), languageData!!.PVENUES)
        adapter.addFragment(AMusicTypeFragment.newInstance(), languageData!!.PMUSIC)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun validateError(message: String) {
       Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

    private fun setupViews() {
        val tabs = findViewById<TabLayout>(R.id.near_me_tabs)
        val viewPager = findViewById<ViewPager>(R.id.near_me_viewPager)
        setupAdapter(viewPager, tabs)
    }


}
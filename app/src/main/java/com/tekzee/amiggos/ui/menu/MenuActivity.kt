package com.tekzee.amiggos.ui.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MenuFragmentBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.ui.finalbasket.FinalBasketActivity
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.menu.commonfragment.CommonFragment
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.util.Coroutines
import com.tekzee.amiggos.util.Errortoast
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat

class MenuActivity : AppCompatActivity(), MenuEvent, KodeinAware {

    private lateinit var responseData: MenuResponse
    override val kodein: Kodein by closestKodein()
    val languageConstant: LanguageData by instance<LanguageData>()
    val prefs: SharedPreference by instance<SharedPreference>()
    val factory: MenuModelFactory by instance<MenuModelFactory>()
//    private val df2: DecimalFormat = DecimalFormat("#.##")

    companion object {
        fun newInstance() = MenuActivity()
    }
    var totalAmout = 0.0
    private var binding: MenuFragmentBinding? = null
    private lateinit var viewModel: MenuViewModel
    private var repository: ItemRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.menu_fragment)
        viewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)
        binding!!.staffviewmodel = viewModel
        viewModel.menuEvent = this
        binding!!.headertitle.text = languageConstant.selectyourpacakge
        setupclickListener()
        Log.e("venue id------>", intent.getStringExtra(ConstantLib.VENUE_ID)!!)
        setupAmount()
    }

    override fun onResume() {
        super.onResume()
        viewModel.callMenuApi(
            intent.getStringExtra(ConstantLib.VENUE_ID), intent.getStringExtra(
                ConstantLib.DATE
            ), intent.getStringExtra(ConstantLib.TIME)
        )
    }

    private fun setupAmount() {
        val itemDao = AmiggoRoomDatabase.getDatabase(this).itemDao()
        repository = ItemRepository(itemDao)
        Coroutines.main {
            repository!!.getTotalCartAmount(intent.getStringExtra(ConstantLib.VENUE_ID)!!).observe(
                this,
                Observer {
                    totalAmout = 0.0
                    for (item in it) {
                        totalAmout += (item.quantity * item.price)
                    }
                    binding!!.headerAmount.text = "Total "+Utility.formatCurrency(totalAmout.toFloat())
                })
        }

    }

    private fun setupclickListener() {
        binding!!.drawerIcon.setOnClickListener {
            onBackPressed()
        }

        binding!!.btnNextCart.setOnClickListener {
            if(totalAmout>0){
                val intentFinalBasket = Intent(applicationContext, FinalBasketActivity::class.java)
                intentFinalBasket.putExtra(ConstantLib.TAX, responseData.data.tax)
                intentFinalBasket.putExtra(
                    ConstantLib.TIME,
                    intent.getStringExtra(ConstantLib.TIME)
                )
                intentFinalBasket.putExtra(
                    ConstantLib.DATE,
                    intent.getStringExtra(ConstantLib.DATE)
                )
                intentFinalBasket.putExtra(
                    ConstantLib.VENUE_ID,
                    intent.getStringExtra(ConstantLib.VENUE_ID)
                )
                intentFinalBasket.putExtra(
                    ConstantLib.SELECTED_VENUE_DIN_TOGO, intent.getStringExtra(
                        ConstantLib.SELECTED_VENUE_DIN_TOGO
                    )
                )
                startActivity(intentFinalBasket)
            }else{
                Errortoast(languageConstant.selectanyonemenuitem)
            }

        }
    }


    private fun setupAdapter(
        menuList: List<MenuResponse.Data.Section>,
        staffViewPager: ViewPager,
        staffTabs: TabLayout
    ) {
        val fragmentManager =supportFragmentManager
        val adapter = ViewPagerAdapter(fragmentManager)
        for (item in menuList) {
            adapter.addFragment(CommonFragment.newInstance(item.id, item), item.name)
        }
//        staffViewPager.adapter = adapter
        staffViewPager.adapter = adapter
        staffTabs.setupWithViewPager(staffViewPager)

    }

    override fun onStarted() {
        binding!!.progressCircular.visibility = View.VISIBLE
    }

    override fun onLoaded(menuList: List<MenuResponse.Data.Section>, response: MenuResponse) {
        binding!!.progressCircular.visibility = View.GONE
        setupAdapter(menuList, binding!!.viewpager, binding!!.staffTabs)
        responseData = response
    }

    override fun onFailure(message: String) {
        binding!!.progressCircular.visibility = View.GONE
        Errortoast(message)
    }

    override fun sessionExpired(message: String) {
        Errortoast(message)
    }

    override fun onDrawerClicked() {

    }

}

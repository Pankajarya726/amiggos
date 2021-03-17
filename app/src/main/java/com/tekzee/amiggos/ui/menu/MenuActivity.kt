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
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.base.repository.ItemRepository
import com.tekzee.amiggos.constant.ConstantLib
import com.tekzee.amiggos.databinding.MenuFragmentBinding
import com.tekzee.amiggos.room.database.AmiggoRoomDatabase
import com.tekzee.amiggos.room.entity.Menu
import com.tekzee.amiggos.ui.finalbasket.FinalBasketActivity
import com.tekzee.amiggos.ui.finalbasket.model.CreateBookingResponse
import com.tekzee.amiggos.ui.homescreen_new.nearmefragment.adapter.ViewPagerAdapter
import com.tekzee.amiggos.ui.invitefriendnew.InviteFriendNewActivity
import com.tekzee.amiggos.ui.menu.commonfragment.CommonFragment
import com.tekzee.amiggos.ui.menu.model.MenuResponse
import com.tekzee.amiggos.ui.stripepayment.paymentactivity.PaymentActivity
import com.tekzee.amiggos.util.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat

class MenuActivity : AppCompatActivity(), MenuEvent, KodeinAware {
    private lateinit var newlist: java.util.ArrayList<Menu>
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
                    newlist = ArrayList<Menu>()
                    newlist.addAll(it)
                    totalAmout = 0.0
                    for (item in it) {
                        totalAmout += (item.quantity * item.price)
                    }
                    binding!!.headerAmount.text =
                        "Total " + Utility.formatCurrency(totalAmout.toFloat())
                })
        }

    }

    private fun setupclickListener() {
        binding!!.drawerIcon.setOnClickListener {
            onBackPressed()
        }

        binding!!.btnNextCart.setOnClickListener {
            if (newlist.isNotEmpty()) {
                if (totalAmout > 0) {
                    val intentFinalBasket =
                        Intent(applicationContext, FinalBasketActivity::class.java)
                    intentFinalBasket.putExtra(ConstantLib.TAX, responseData.data.tax)
                    intentFinalBasket.putExtra(ConstantLib.ALLOW_INVITE, intent.getStringExtra(ConstantLib.ALLOW_INVITE))
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
                    if(intent.getStringExtra(ConstantLib.FROM).equals(ConstantLib.ADDITEMS)){
                        intentFinalBasket.putExtra(
                            ConstantLib.FROM,
                            ConstantLib.ADDITEMS
                        )
                    }else{
                        intentFinalBasket.putExtra(
                            ConstantLib.FROM,
                            ConstantLib.MENUACTIVITY
                        )
                    }

                    intentFinalBasket.putExtra(
                        ConstantLib.BOOKING_ID,
                        intent.getStringExtra(ConstantLib.BOOKING_ID)
                    )


                    startActivity(intentFinalBasket)
                } else {
                    callCreateBookingApi()
                }
            } else {
                Errortoast(languageConstant.selectanyonemenuitem)
            }
        }
    }


    private fun callCreateBookingApi() {
        val input = JsonObject()
        input.addProperty("userid", prefs.getValueInt(ConstantLib.USER_ID))
        input.addProperty("venue_id", intent.getStringExtra(ConstantLib.VENUE_ID))
        input.addProperty("sub_total", "0.00")
        input.addProperty("booking_date", intent.getStringExtra(ConstantLib.DATE))
        input.addProperty(
            "booking_method",
            intent.getStringExtra(ConstantLib.SELECTED_VENUE_DIN_TOGO)
        )
        input.addProperty("booking_time", intent.getStringExtra(ConstantLib.TIME))
        input.addProperty("instruction", "")
        input.add("menu_item", createMenuItems())
        input.addProperty("price_category", "$$$")
        input.addProperty("tax", "0.00")
        input.addProperty("tip", "0.00")
        input.addProperty("total_amount", "0.00")
        if(intent.getStringExtra(ConstantLib.FROM).equals(ConstantLib.ADDITEMS)){
            input.addProperty("bookingid", intent.getStringExtra(ConstantLib.BOOKING_ID))
        }else{
            input.addProperty("bookingid", "")
        }

        viewModel.callCreateBookingApi(input)
    }

    private fun createMenuItems(): JsonArray {

        val jsonArray = JsonArray()
        for (item in newlist) {
            val jsonobject = JsonObject()
            jsonobject.addProperty("item_id", item.id)
            jsonobject.addProperty("name", item.name)
            jsonobject.addProperty("price", item.price)
            jsonobject.addProperty("qty", item.quantity)
            jsonArray.add(jsonobject)
        }
        return jsonArray
    }

    private fun setupAdapter(
        menuList: List<MenuResponse.Data.Section>,
        staffViewPager: ViewPager,
        staffTabs: TabLayout
    ) {
        val fragmentManager = supportFragmentManager
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

    override fun onCreateBookingSuccess(response: CreateBookingResponse) {
        hideProgressBar()

        if(response.data.booking.amount>0.0){
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra(ConstantLib.FROM, ConstantLib.FINALBASKET)
            intent.putExtra(ConstantLib.PURCHASE_AMOUNT, response.data.booking.amount.toString())
            intent.putExtra(ConstantLib.BOOKING_ID, response.data.booking.bookingId.toString())
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }else{
            Coroutines.main {
                repository!!.clearCart()
            }
            val intent = Intent(applicationContext, InviteFriendNewActivity::class.java)
            intent.putExtra(ConstantLib.MESSAGE,response.message)
            intent.putExtra(ConstantLib.FROM,ConstantLib.FINALBASKET)
            intent.putExtra(ConstantLib.BOOKING_ID,response.data.booking.bookingId.toString())
            startActivity(intent)
        }


    }

    override fun onCreateBookingFailure(message: String) {
        hideProgressBar()
        Errortoast(message)
    }

    private fun getCartItems(tip: Float) {
        Coroutines.main {
            repository!!.getItemCount(intent.getStringExtra(ConstantLib.VENUE_ID)!!).observe(
                this,
                Observer {
                    Log.e("item list count-->", it.size.toString())
                    newlist = ArrayList<Menu>()
                    newlist.addAll(it)
                })
        }
    }

}

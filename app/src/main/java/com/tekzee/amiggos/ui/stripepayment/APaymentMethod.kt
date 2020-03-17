package com.tekzee.amiggos.ui.stripepayment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.JsonObject
import com.tekzee.amiggos.R
import com.tekzee.amiggos.base.BaseActivity
import com.tekzee.amiggos.base.model.LanguageData
import com.tekzee.amiggos.databinding.APaymentActivityBinding
import com.tekzee.amiggos.ui.stripepayment.adapter.PaymentAdapter
import com.tekzee.amiggos.ui.stripepayment.addnewcard.AAddCard
import com.tekzee.amiggos.ui.stripepayment.model.CardListResponse
import com.tekzee.amiggos.util.SharedPreference
import com.tekzee.amiggos.util.Utility
import com.tekzee.mallortaxiclient.constant.ConstantLib
import java.util.*

class APaymentMethod: BaseActivity(), APaymentMethodPresenter.APaymentMethodPresenterMainView {

    private var adapter: PaymentAdapter? = null
    private var binding: APaymentActivityBinding?=null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aPaymentMehtodImplementation: APaymentMehtodImplementation? =null
    private val data = ArrayList<CardListResponse.Cards.Card>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_payment_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aPaymentMehtodImplementation = APaymentMehtodImplementation(this,this)
        setupToolBar()
        setupClickListener()

        setUpRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        data.clear()
        adapter!!.notifyDataSetChanged()
        callGetCardList()
    }


    private fun setUpRecyclerView() {
        binding!!.cardRecyclerview.setHasFixedSize(true)
        binding!!.cardRecyclerview.setLayoutManager(
            LinearLayoutManager(
               this
            )
        )
        adapter = PaymentAdapter(data, object : PaymentClick {
            override fun onRowClick(model: CardListResponse.Cards.Card) {
                //                val intent = Intent(
//                    this,
//                    CardDetailsActivity::class.java
//                )
//                intent.putExtra("id", model.getId())
//                intent.putExtra("Card_Type", model.getBrand())
//                intent.putExtra("isDefault", model.getIsDefault())
//                intent.putExtra("Card_Number", model.getLast4())
//                intent.putExtra("Expiry_Month", model.getExpiryMonth())
//                intent.putExtra("Finger_Print", model.getFingerPrint())
//                intent.putExtra("Expiry_Year", model.getExpiryYear())
//                startActivity(intent)
            }
        })
        binding!!.cardRecyclerview.adapter = adapter

    }


    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this)
    }

    private fun setupClickListener() {
        binding!!.addcard.setOnClickListener {
            val intent = Intent(this, AAddCard ::class.java)
            startActivity(intent)
        }
    }

    private fun setupToolBar() {
        val toolbar: Toolbar = binding!!.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    fun callGetCardList() {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        aPaymentMehtodImplementation!!.getCardList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onCardListSuccess(responseData: List<CardListResponse.Cards.Card>) {
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}
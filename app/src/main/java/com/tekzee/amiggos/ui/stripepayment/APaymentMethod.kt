package com.tekzee.amiggos.ui.stripepayment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
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
import com.tekzee.amiggos.constant.ConstantLib
import java.util.*

class APaymentMethod: BaseActivity(), APaymentMethodPresenter.APaymentMethodPresenterMainView {

    private var customStripeId: String? = ""
    private var adapter: PaymentAdapter? = null
    private var binding: APaymentActivityBinding?=null
    private var sharedPreferences: SharedPreference? = null
    private var languageData: LanguageData? = null
    private var aPaymentMehtodImplementation: APaymentMehtodImplementation? =null
    private val data = ArrayList<CardListResponse.Data.Card>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.a_payment_activity)
        sharedPreferences = SharedPreference(this)
        languageData = sharedPreferences!!.getLanguageData(ConstantLib.LANGUAGE_DATA)
        aPaymentMehtodImplementation = APaymentMehtodImplementation(this,this)
        setupToolBar()
        setupClickListener()
        setupLanguage()
        setUpRecyclerView()

    }

    private fun setupLanguage() {
        binding!!.addcard.text = languageData!!.addcard
        binding!!.savedCards.text = languageData!!.savedcards
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
        adapter = PaymentAdapter(data,intent.getStringExtra(ConstantLib.FROM), object : PaymentClick {
            override fun onRowClick(model: CardListResponse.Data.Card) {
                
            }

            override fun onDeleteCard(model: CardListResponse.Data.Card?) {
                val pDialog =
                    SweetAlertDialog(this@APaymentMethod, SweetAlertDialog.WARNING_TYPE)
                //change language
                pDialog.titleText = languageData!!.deletecard

                pDialog.setCancelable(false)
                pDialog.setCancelButton(languageData!!.klCancel) {
                    pDialog.dismiss()
                }
                pDialog.setConfirmButton(languageData!!.klOk) {
                    pDialog.dismiss()
                     callDeleteCard(model)
                }
                pDialog.show()
            }
        })
        binding!!.cardRecyclerview.adapter = adapter

    }


    fun callDeleteCard(model: CardListResponse.Data.Card?) {
        val input: JsonObject = JsonObject()
        input.addProperty("userid", sharedPreferences!!.getValueInt(ConstantLib.USER_ID))
        input.addProperty("strip_customerId", customStripeId)
        input.addProperty("card_Id", model!!.id)
        input.addProperty("finger_print", model.fingerPrint)
        input.addProperty("user_type", sharedPreferences!!.getValueString(ConstantLib.USER_TYPE))
        aPaymentMehtodImplementation!!.deleteCardApi(
            input,
            Utility.createHeaders(sharedPreferences)
        )
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
        input.addProperty("user_type", sharedPreferences!!.getValueString(ConstantLib.USER_TYPE))
        aPaymentMehtodImplementation!!.getCardList(
            input,
            Utility.createHeaders(sharedPreferences)
        )
    }

    override fun onCardListSuccess(
        responseData: List<CardListResponse.Data.Card>,
        customerStripId: String
    ) {
        customStripeId = customerStripId;
        data.clear()
        adapter!!.notifyDataSetChanged()
        data.addAll(responseData)
        adapter!!.notifyDataSetChanged()
    }

    override fun onCardDeleteSuccess(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
        data.clear()
        adapter!!.notifyDataSetChanged()
        callGetCardList()
    }


    override fun validateError(message: String) {
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}